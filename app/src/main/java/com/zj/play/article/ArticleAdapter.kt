package com.zj.play.article

import android.content.Context
import android.text.TextUtils
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.zj.core.Play
import com.zj.core.util.getHtmlText
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.core.view.base.BaseListAdapter
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.HISTORY
import com.zj.network.repository.CollectRepository
import com.zj.play.R
import com.zj.play.main.LoginActivity
import kotlinx.android.synthetic.main.adapter_article.*
import kotlinx.coroutines.*


class ArticleAdapter(
    context: Context,
    articleList: ArrayList<Article>,
    private val isShowCollect: Boolean = true,
    layoutId: Int = R.layout.adapter_article,
) : BaseListAdapter<Article>(context, layoutId, articleList) {

    private val uiScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun convert(holder: ViewHolder, data: Article, position: Int) {
        with(holder) {
            if (!TextUtils.isEmpty(data.title))
                articleTvTitle.text = getHtmlText(data.title)
            articleTvChapterName.text = data.superChapterName
            articleTvAuthor.text =
                if (TextUtils.isEmpty(data.author)) data.shareUser else data.author
            articleTvTime.text = data.niceShareDate
            if (!TextUtils.isEmpty(data.envelopePic)) {
                articleIvImg.visibility = VISIBLE
                Glide.with(mContext).load(data.envelopePic).into(articleIvImg)
            } else {
                articleIvImg.visibility = GONE
            }
            articleTvTop.visibility = if (data.type > 0) VISIBLE else GONE
            articleTvNew.visibility = if (data.fresh) VISIBLE else GONE

            articleIvCollect.visibility = if (isShowCollect) VISIBLE else GONE
            if (data.collect) {
                articleIvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                articleIvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
            articleIvCollect.setSafeListener {
                if (Play.isLogin) {
                    if (NetworkUtils.isConnected()) {
                        data.collect = !data.collect
                        setCollect(data, articleIvCollect)
                    } else {
                        showToast(mContext.getString(R.string.no_network))
                    }
                } else {
                    LoginActivity.actionStart(mContext)
                }
            }
            articleLlItem.setOnClickListener {
                if (!NetworkUtils.isConnected()) {
                    showToast(mContext.getString(R.string.no_network))
                    return@setOnClickListener
                }
                ArticleActivity.actionStart(mContext, data)
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
