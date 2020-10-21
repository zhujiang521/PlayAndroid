package com.zj.play.view.profile.history

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.zj.core.util.showToast
import com.zj.play.view.article.ArticleAdapter
import com.zj.play.view.base.BaseListActivity
import kotlinx.android.synthetic.main.activity_base_list.*

class BrowseHistoryActivity : BaseListActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(BrowseHistoryViewModel::class.java) }
    private lateinit var articleAdapter: ArticleAdapter

    override fun initView() {
        super.initView()
        articleAdapter = ArticleAdapter(
            this,
            viewModel.articleList,
            false
        )
        articleAdapter.setHasStableIds(true)
        baseListRecycleView.adapter = articleAdapter
        baseListTitleBar.setTitle("浏览历史")
    }

    override fun isStaggeredGrid(): Boolean {
        return true
    }

    override fun getDataList() {
        if (viewModel.articleList.size <= 0) {
            startLoading()
        }
        viewModel.getArticleList(page)
    }

    override fun initData() {
        super.initData()
        viewModel.articleLiveData.observe(this, {
            if (it.isSuccess) {
                val articleList = it.getOrNull()
                if (articleList != null) {
                    loadFinished()
                    if (page == 1 && viewModel.articleList.size > 0) {
                        viewModel.articleList.clear()
                    }
                    viewModel.articleList.addAll(articleList)
                    articleAdapter.notifyDataSetChanged()
                } else {
                    showLoadErrorView()
                }
            } else {
                if (viewModel.articleList.size <= 0) {
                    showNoContentView("当前无历史浏览记录")
                } else {
                    showToast("没有更多数据")
                    loadFinished()
                }
            }
        })
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, BrowseHistoryActivity::class.java)
            context.startActivity(intent)
        }
    }

}