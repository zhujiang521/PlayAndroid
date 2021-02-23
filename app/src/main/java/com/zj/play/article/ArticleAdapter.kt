package com.zj.play.article

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.zj.core.Play
import com.zj.core.util.ProgressDialogUtil
import com.zj.core.util.getHtmlText
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.HISTORY
import com.zj.play.R
import com.zj.play.article.collect.CollectRepository
import com.zj.play.article.collect.CollectRepositoryPoint
import com.zj.play.databinding.AdapterArticleBinding
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.*

class ArticleAdapter(
    private val mContext: Context,
    private val articleList: ArrayList<Article>,
    private val isShowCollect: Boolean = true,
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private val uiScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var progressDialogUtil: ProgressDialogUtil = ProgressDialogUtil.getInstance(mContext)!!

    inner class ViewHolder(binding: AdapterArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        val articleTvTitle: TextView = binding.articleTvTitle
        val articleTvChapterName: TextView = binding.articleTvChapterName
        val articleTvAuthor: TextView = binding.articleTvAuthor
        val articleTvTime: TextView = binding.articleTvTime
        val articleIvImg: ImageView = binding.articleIvImg
        val articleTvTop: TextView = binding.articleTvTop
        val articleTvNew: TextView = binding.articleTvNew
        val articleIvCollect: ImageView = binding.articleIvCollect
        val articleLlItem: RelativeLayout = binding.articleLlItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleAdapter.ViewHolder {
        val binding =
            AdapterArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    private fun setCollect(
        collectRepository: CollectRepository,
        t: Article,
        articleTvCollect: ImageView
    ) {
        progressDialogUtil.progressDialogShow(
            if (t.collect) mContext.getString(R.string.bookmarking) else mContext.getString(
                R.string.unfavorite
            )
        )
        uiScope.launch {
            val articleDao = PlayDatabase.getDatabase(mContext).browseHistoryDao()
            if (!t.collect) {
                val cancelCollects = collectRepository.cancelCollects(t.id)
                if (cancelCollects.errorCode == 0) {
                    withContext(Dispatchers.Main) {
                        articleTvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                        showToast(mContext.getString(R.string.collection_cancelled_successfully))
                        articleDao.update(t)
                        progressDialogUtil.progressDialogDismiss()
                    }
                } else {
                    showToast(mContext.getString(R.string.failed_to_cancel_collection))
                    progressDialogUtil.progressDialogDismiss()
                }
            } else {
                val toCollects = collectRepository.toCollects(t.id)
                if (toCollects.errorCode == 0) {
                    withContext(Dispatchers.Main) {
                        articleTvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
                        showToast(mContext.getString(R.string.collection_successful))
                        articleDao.update(t)
                        progressDialogUtil.progressDialogDismiss()
                    }
                } else {
                    showToast(mContext.getString(R.string.collection_failed))
                    progressDialogUtil.progressDialogDismiss()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = articleList[position]
        val collectRepository = EntryPointAccessors.fromApplication(
            mContext,
            CollectRepositoryPoint::class.java
        ).collectRepository()
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
            articleTvTop.isVisible = data.type > 0
            articleTvNew.isVisible = data.fresh

            articleIvCollect.isVisible = isShowCollect
            if (data.collect) {
                articleIvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                articleIvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
            articleIvCollect.setSafeListener {
                if (Play.isLogin) {
                    if (NetworkUtils.isConnected()) {
                        data.collect = !data.collect
                        setCollect(collectRepository, data, articleIvCollect)
                    } else {
                        showToast(mContext.getString(R.string.no_network))
                    }
                } else {
                    showToast(mContext.getString(R.string.not_currently_logged_in))
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

    override fun getItemCount(): Int {
        return articleList.size
    }

}
