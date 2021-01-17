package com.zj.play.article.collect

import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.zj.core.util.getHtmlText
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.core.view.base.BaseListAdapter
import com.zj.model.model.CollectX
import com.zj.play.R
import com.zj.play.article.ArticleActivity
import dagger.hilt.android.EntryPointAccessors
import kotlinx.android.synthetic.main.adapter_article.*
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

    override fun convert(holder: ViewHolder, data: CollectX, position: Int) {
        with(holder) {
            articleTvTitle.text = getHtmlText(data.title)
            articleTvChapterName.text = data.chapterName
            articleTvAuthor.text =
                if (TextUtils.isEmpty(data.author)) data.chapterName else data.author
            articleTvTime.text = data.niceDate
            if (!TextUtils.isEmpty(data.envelopePic)) {
                articleIvImg.visibility = View.VISIBLE
                Glide.with(mContext).load(data.envelopePic).into(articleIvImg)
            } else {
                articleIvImg.visibility = View.GONE
            }
            articleTvTop.visibility = View.GONE
            articleTvNew.visibility = View.GONE
            articleIvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
            articleIvCollect.setSafeListener {
                cancelCollect(data.originId, position)
            }
            articleLlItem.setOnClickListener {
                if (!NetworkUtils.isConnected()) {
                    showToast(mContext.getString(R.string.no_network))
                    return@setOnClickListener
                }
                ArticleActivity.actionStart(mContext, data)
            }
        }
    }

    private fun cancelCollect(id: Int, position: Int) {
        lifecycleScope.launch {
            val collectRepository =
                EntryPointAccessors.fromApplication(mContext, CollectRepositoryPoint::class.java)
                    .collectRepository()
            val cancelCollects = collectRepository.cancelCollects(id)
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

