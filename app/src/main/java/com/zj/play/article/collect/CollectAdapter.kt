package com.zj.play.article.collect

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.zj.core.util.getHtmlText
import com.zj.core.util.setSafeListener
import com.zj.core.util.showToast
import com.zj.core.view.base.BaseRecyclerAdapter
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
) : BaseRecyclerAdapter<AdapterArticleBinding>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerHolder<AdapterArticleBinding> {
        val binding =
            AdapterArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseRecyclerHolder(binding)
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
                    articleList.removeAt(position)
                    notifyItemChanged(position)
                } else {
                    showToast(mContext.getString(R.string.failed_to_cancel_collection))
                }
            }
        }
    }

    override fun onBaseBindViewHolder(position: Int, binding: AdapterArticleBinding) {
        val data = articleList[position]
        binding.apply {
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

