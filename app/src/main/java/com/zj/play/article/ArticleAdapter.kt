package com.zj.play.article

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.zj.core.Play
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.core.view.base.BaseListAdapter
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
) : BaseListAdapter<Article>(context, layoutId, articleList) {

    private val uiScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun convert(view: View, data: Article, position: Int) {
        if (!TextUtils.isEmpty(data.title))
            view.articleTvTitle.text =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(data.title, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    data.title
                }
        view.articleTvChapterName.text = data.superChapterName
        view.articleTvAuthor.text =
            if (TextUtils.isEmpty(data.author)) data.shareUser else data.author
        view.articleTvTime.text = data.niceShareDate
        if (!TextUtils.isEmpty(data.envelopePic)) {
            view.articleIvImg.visibility = View.VISIBLE
            Glide.with(mContext).load(data.envelopePic).into(view.articleIvImg)
        } else {
            view.articleIvImg.visibility = View.GONE
        }
        view.articleTvTop.visibility = if (data.type > 0) View.VISIBLE else View.GONE
        view.articleTvNew.visibility = if (data.fresh) View.VISIBLE else View.GONE

        view.articleIvCollect.visibility = if (isShowCollect) View.VISIBLE else View.GONE
        if (data.collect) {
            view.articleIvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
        } else {
            view.articleIvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }
        view.articleIvCollect.setSafeListener {
            if (Play.isLogin) {
                if (NetworkUtils.isConnected()) {
                    data.collect = !data.collect
                    setCollect(data, view.articleIvCollect)
                } else {
                    showToast(mContext.getString(R.string.no_network))
                }
            } else {
                LoginActivity.actionStart(mContext)
            }
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
                if (data.collect) 1 else 0,
                userId = data.userId
            )
            val browseHistoryDao = PlayDatabase.getDatabase(mContext).browseHistoryDao()
            uiScope.launch {
                if (browseHistoryDao.getArticle(data.id, HISTORY) == null) {
                    data.localType = HISTORY
                    data.desc = ""
                    browseHistoryDao.insert(data)
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
