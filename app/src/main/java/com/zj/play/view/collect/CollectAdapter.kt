package com.zj.play.view.collect

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.play.R
import com.zj.play.model.CollectX
import com.zj.play.network.CollectRepository
import com.zj.play.view.article.ArticleActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectAdapter(context: Context, layoutId: Int, articleList: ArrayList<CollectX>) :
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
        articleTvTitle.text = Html.fromHtml(t.title)
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
                showToast("当前网络不可用")
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
        GlobalScope.launch {
            val cancelCollects = CollectRepository.cancelCollects(id)
            withContext(Dispatchers.Main) {
                if (cancelCollects.errorCode == 0) {
                    showToast("取消收藏成功")
                    notifyItemRemoved(position)
                } else {
                    showToast("取消收藏失败")
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}

