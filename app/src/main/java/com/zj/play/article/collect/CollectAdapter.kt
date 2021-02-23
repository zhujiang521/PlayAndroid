package com.zj.play.article.collect

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.zj.core.util.getHtmlText
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.core.view.base.BaseListAdapter
import com.zj.model.model.CollectX
import com.zj.play.R
import com.zj.play.article.ArticleActivity
import com.zj.play.databinding.AdapterArticleBinding
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectAdapter(
    private val mContext: Context,
    private val articleList: ArrayList<CollectX>,
    private val lifecycleScope: LifecycleCoroutineScope,
) : RecyclerView.Adapter<CollectAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectAdapter.ViewHolder {
        val binding =
            AdapterArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = articleList[position]
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

    override fun getItemCount(): Int {
        return articleList.size
    }

}

