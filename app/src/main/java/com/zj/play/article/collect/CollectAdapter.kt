package com.zj.play.article.collect

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.play.R
import com.zj.model.model.CollectX
import com.zj.network.repository.CollectRepository
import com.zj.play.article.ArticleActivity
import kotlinx.coroutines.*

class CollectAdapter(
    context: Context,
    articleList: ArrayList<CollectX>,
    private val lifecycleScope: LifecycleCoroutineScope,
    layoutId: Int = R.layout.adapter_article
) :
    CommonAdapter<CollectX>(context, layoutId, articleList) {

    override fun convert(holder: ViewHolder, t: CollectX, position: Int) {
        val articleLlItem = holder.getView<RelativeLayout>(R.id.articleLlItem)
        val articleIvImg = holder.getView<ImageView>(R.id.articleIvImg)
        val articleTvAuthor = holder.getView<TextView>(R.id.articleTvAuthor)
        val articleTvNew = holder.getView<TextView>(R.id.articleTvNew)
        val articleTvTop = holder.getView<TextView>(R.id.articleTvTop)
        val articleTvTime = holder.getView<TextView>(R.id.articleTvTime)
        val articleTvTitle = holder.getView<TextView>(R.id.articleTvTitle)
        val articleTvChapterName = holder.getView<TextView>(R.id.articleTvChapterName)
        val articleTvCollect = holder.getView<ImageView>(R.id.articleIvCollect)
        articleTvTitle.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(t.title, Html.FROM_HTML_MODE_LEGACY)
            } else {
                t.title
            }
        articleTvChapterName.text = t.chapterName
        articleTvAuthor.text = if (TextUtils.isEmpty(t.author)) t.chapterName else t.author
        articleTvTime.text = t.niceDate
        if (!TextUtils.isEmpty(t.envelopePic)) {
            articleIvImg.visibility = View.VISIBLE
            Glide.with(mContext).load(t.envelopePic).into(articleIvImg)
        } else {
            articleIvImg.visibility = View.GONE
        }
        articleTvTop.visibility = View.GONE
        articleTvNew.visibility = View.GONE
        articleTvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
        articleTvCollect.setSafeListener {
            cancelCollect(t.originId, position)
        }
        articleLlItem.setOnClickListener {
            if (!NetworkUtils.isConnected()) {
                showToast(mContext.getString(R.string.no_network))
                return@setOnClickListener
            }
            ArticleActivity.actionStart(
                mContext,
                t.title,
                t.link,
                t.id,
                1,
                t.originId,
                userId = t.userId
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}

