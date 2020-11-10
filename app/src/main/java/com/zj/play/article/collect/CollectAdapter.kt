package com.zj.play.article.collect

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.core.view.custom.BaseCommonAdapter
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
    BaseCommonAdapter<CollectX>(context, layoutId, articleList) {

    override fun convert(holder: ViewHolder, t: CollectX, position: Int) {
        holder.itemView.articleTvTitle.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(t.title, Html.FROM_HTML_MODE_LEGACY)
            } else {
                t.title
            }
        holder.itemView.articleTvChapterName.text = t.chapterName
        holder.itemView.articleTvAuthor.text =
            if (TextUtils.isEmpty(t.author)) t.chapterName else t.author
        holder.itemView.articleTvTime.text = t.niceDate
        if (!TextUtils.isEmpty(t.envelopePic)) {
            holder.itemView.articleIvImg.visibility = View.VISIBLE
            Glide.with(mContext).load(t.envelopePic).into(holder.itemView.articleIvImg)
        } else {
            holder.itemView.articleIvImg.visibility = View.GONE
        }
        holder.itemView.articleTvTop.visibility = View.GONE
        holder.itemView.articleTvNew.visibility = View.GONE
        holder.itemView.articleIvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
        holder.itemView.articleIvCollect.setSafeListener {
            cancelCollect(t.originId, position)
        }
        holder.itemView.articleLlItem.setOnClickListener {
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

}

