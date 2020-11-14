package com.zj.play.article.collect

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.core.view.base.BaseListAdapter
import com.zj.model.model.CollectX
import com.zj.network.repository.CollectRepository
import com.zj.play.R
import com.zj.play.article.ArticleActivity
import kotlinx.android.synthetic.main.adapter_article.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectAdapter(
    context: Context,
    articleList: ArrayList<CollectX>,
    private val lifecycleScope: LifecycleCoroutineScope,
    layoutId: Int = R.layout.adapter_article
) :
    BaseListAdapter<CollectX>(context, layoutId, articleList) {

    override fun convert(view: View, data: CollectX, position: Int) {
        view.articleTvTitle.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(data.title, Html.FROM_HTML_MODE_LEGACY)
            } else {
                data.title
            }
        view.articleTvChapterName.text = data.chapterName
        view.articleTvAuthor.text =
            if (TextUtils.isEmpty(data.author)) data.chapterName else data.author
        view.articleTvTime.text = data.niceDate
        if (!TextUtils.isEmpty(data.envelopePic)) {
            view.articleIvImg.visibility = View.VISIBLE
            Glide.with(mContext).load(data.envelopePic).into(view.articleIvImg)
        } else {
            view.articleIvImg.visibility = View.GONE
        }
        view.articleTvTop.visibility = View.GONE
        view.articleTvNew.visibility = View.GONE
        view.articleIvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
        view.articleIvCollect.setSafeListener {
            cancelCollect(data.originId, position)
        }
        view.articleLlItem.setOnClickListener {
            if (!NetworkUtils.isConnected()) {
                showToast(mContext.getString(R.string.no_network))
                return@setOnClickListener
            }
            ArticleActivity.actionStart(
                mContext,
                data.title,
                data.link,
                data.id,
                1,
                data.originId,
                userId = data.userId
            )
        }
    }

    private fun cancelCollect(id: Int, position: Int) {
        lifecycleScope.launch {
            val cancelCollects = CollectRepository.cancelCollects(id)
            withContext(Dispatchers.Main) {
                if (cancelCollects.errorCode == 0) {
                    showToast(mContext.getString(R.string.collection_cancelled_successfully))
                    notifyItemRemoved(position)
                } else {
                    showToast(mContext.getString(R.string.failed_to_cancel_collection))
                }
            }
        }
    }

}

