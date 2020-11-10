package com.zj.play.article

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.core.Play
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.core.view.custom.BaseCommonAdapter
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.HISTORY
import com.zj.network.repository.CollectRepository
import com.zj.play.R
import com.zj.play.main.LoginActivity
import kotlinx.android.synthetic.main.adapter_article.view.*
import kotlinx.coroutines.*


class ArticleAdapter(
    context: Context,
    articleList: ArrayList<Article>,
    private val isShowCollect: Boolean = true,
    layoutId: Int = R.layout.adapter_article,
) :
    BaseCommonAdapter<Article>(context, layoutId, articleList) {

    private val uiScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun convert(holder: ViewHolder, t: Article, position: Int) {
        if (!TextUtils.isEmpty(t.title))
            holder.itemView.articleTvTitle.text =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(t.title, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    t.title
                }
        holder.itemView.articleTvChapterName.text = t.superChapterName
        holder.itemView.articleTvAuthor.text =
            if (TextUtils.isEmpty(t.author)) t.shareUser else t.author
        holder.itemView.articleTvTime.text = t.niceShareDate
        if (!TextUtils.isEmpty(t.envelopePic)) {
            holder.itemView.articleIvImg.visibility = View.VISIBLE
            Glide.with(mContext).load(t.envelopePic).into(holder.itemView.articleIvImg)
        } else {
            holder.itemView.articleIvImg.visibility = View.GONE
        }
        holder.itemView.articleTvTop.visibility = if (t.type > 0) View.VISIBLE else View.GONE
        holder.itemView.articleTvNew.visibility = if (t.fresh) View.VISIBLE else View.GONE

        holder.itemView.articleIvCollect.visibility = if (isShowCollect) View.VISIBLE else View.GONE
        if (t.collect) {
            holder.itemView.articleIvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
        } else {
            holder.itemView.articleIvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }
        holder.itemView.articleIvCollect.setSafeListener {
            if (Play.isLogin) {
                if (NetworkUtils.isConnected()) {
                    t.collect = !t.collect
                    setCollect(t, holder.itemView.articleIvCollect)
                } else {
                    showToast(mContext.getString(R.string.no_network))
                }
            } else {
                LoginActivity.actionStart(mContext)
            }
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
                if (t.collect) 1 else 0,
                userId = t.userId
            )
            val browseHistoryDao = PlayDatabase.getDatabase(mContext).browseHistoryDao()
            uiScope.launch {
                if (browseHistoryDao.getArticle(t.id, HISTORY) == null) {
                    t.localType = HISTORY
                    t.desc = ""
                    browseHistoryDao.insert(t)
                }
            }
        }
    }

    private fun setCollect(t: Article, articleTvCollect: ImageView) {
        val articleDao = PlayDatabase.getDatabase(mContext).browseHistoryDao()
        uiScope.launch {
            if (!t.collect) {
                val cancelCollects = CollectRepository.cancelCollects(t.id)
                if (cancelCollects.errorCode == 0) {
                    withContext(Dispatchers.Main) {
                        articleTvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                        showToast(mContext.getString(R.string.collection_cancelled_successfully))
                        articleDao.update(t)
                    }
                } else {
                    showToast(mContext.getString(R.string.failed_to_cancel_collection))
                }
            } else {
                val toCollects = CollectRepository.toCollects(t.id)
                if (toCollects.errorCode == 0) {
                    withContext(Dispatchers.Main) {
                        articleTvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
                        showToast(mContext.getString(R.string.collection_successful))
                        articleDao.update(t)
                    }
                } else {
                    showToast(mContext.getString(R.string.collection_failed))
                }

            }
        }
    }

}
