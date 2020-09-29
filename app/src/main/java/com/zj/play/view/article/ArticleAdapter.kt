package com.zj.play.view.article

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.core.Play
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.play.R
import com.zj.play.network.CollectRepository
import com.zj.play.room.PlayDatabase
import com.zj.play.room.entity.Article
import com.zj.play.room.entity.HISTORY
import com.zj.play.view.account.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ArticleAdapter(
    context: Context,
    layoutId: Int,
    articleList: ArrayList<Article>,
    private val isShowCollect: Boolean = true
) :
    CommonAdapter<Article>(context, layoutId, articleList) {

    override fun convert(holder: ViewHolder, t: Article, position: Int) {
        val articleLlItem = holder.getView<RelativeLayout>(R.id.articleLlItem)
        val articleIvImg = holder.getView<ImageView>(R.id.articleIvImg)
        val articleTvAuthor = holder.getView<TextView>(R.id.articleTvAuthor)
        val articleTvNew = holder.getView<TextView>(R.id.articleTvNew)
        val articleTvTop = holder.getView<TextView>(R.id.articleTvTop)
        val articleTvTime = holder.getView<TextView>(R.id.articleTvTime)
        val articleTvTitle = holder.getView<TextView>(R.id.articleTvTitle)
        val articleTvChapterName = holder.getView<TextView>(R.id.articleTvChapterName)
        val articleTvCollect = holder.getView<ImageView>(R.id.articleIvCollect)
        if (!TextUtils.isEmpty(t.title))
            articleTvTitle.text = Html.fromHtml(t.title)
        articleTvChapterName.text = t.superChapterName
        articleTvAuthor.text = if (TextUtils.isEmpty(t.author)) t.shareUser else t.author
        articleTvTime.text = t.niceShareDate
        if (!TextUtils.isEmpty(t.envelopePic)) {
            articleIvImg.visibility = View.VISIBLE
            Glide.with(mContext).load(t.envelopePic).into(articleIvImg)
        } else {
            articleIvImg.visibility = View.GONE
        }
        articleTvTop.visibility = if (t.type > 0) View.VISIBLE else View.GONE
        articleTvNew.visibility = if (t.fresh) View.VISIBLE else View.GONE

        articleTvCollect.visibility = if (isShowCollect) View.VISIBLE else View.GONE
        if (t.collect) {
            articleTvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
        } else {
            articleTvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }
        articleTvCollect.setSafeListener {
            if (Play.isLogin) {
                setCollect(t.id, t.collect, articleTvCollect)
                t.collect = !t.collect
            } else {
                LoginActivity.actionStart(mContext)
            }
        }
        articleLlItem.setOnClickListener {
            ArticleActivity.actionStart(
                mContext,
                t.title,
                t.link,
                t.id,
                if (t.collect) 1 else 0,
                userId = t.userId
            )
            val browseHistoryDao = PlayDatabase.getDatabase(mContext).browseHistoryDao()
            GlobalScope.launch(Dispatchers.IO) {
                if (browseHistoryDao.getArticle(t.id) == null) {
                    t.localType = HISTORY
                    browseHistoryDao.insert(t)
                }
            }
        }
    }

    private fun setCollect(id: Int, collect: Boolean, articleTvCollect: ImageView) {
        GlobalScope.launch {
            if (collect) {
                val cancelCollects = CollectRepository.cancelCollects(id)
                if (cancelCollects.errorCode == 0) {
                    withContext(Dispatchers.Main) {
                        articleTvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                        showToast("取消收藏成功")
                    }
                } else {
                    showToast("取消收藏失败")
                }
            } else {
                val toCollects = CollectRepository.toCollects(id)
                if (toCollects.errorCode == 0) {
                    withContext(Dispatchers.Main) {
                        articleTvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
                        showToast("收藏成功")
                    }
                } else {
                    showToast("收藏失败")
                }

            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
