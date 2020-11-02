package com.zj.play.profile.history

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.zj.core.util.showToast
import com.zj.play.R
import com.zj.play.article.ArticleAdapter
import com.zj.play.base.BaseListActivity
import kotlinx.android.synthetic.main.activity_base_list.*

class BrowseHistoryActivity : BaseListActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(BrowseHistoryViewModel::class.java) }
    private lateinit var articleAdapter: ArticleAdapter

    override fun initView() {
        super.initView()
        articleAdapter = ArticleAdapter(
            this,
            viewModel.dataList,
            false
        )
        articleAdapter.setHasStableIds(true)
        baseListRecycleView.adapter = articleAdapter
        baseListTitleBar.setTitle(getString(R.string.browsing_history))
    }

    override fun isStaggeredGrid(): Boolean {
        return true
    }

    override fun getDataList() {
        if (viewModel.dataList.size <= 0) {
            startLoading()
        }
        viewModel.getDataList(page)
    }

    override fun initData() {
        super.initData()
        viewModel.dataLiveData.observe(this, {
            if (it.isSuccess) {
                val articleList = it.getOrNull()
                if (articleList != null) {
                    loadFinished()
                    if (page == 1 && viewModel.dataList.size > 0) {
                        viewModel.dataList.clear()
                    }
                    viewModel.dataList.addAll(articleList)
                    articleAdapter.notifyDataSetChanged()
                } else {
                    showLoadErrorView()
                }
            } else {
                if (viewModel.dataList.size <= 0) {
                    showNoContentView(getString(R.string.no_browsing_history))
                } else {
                    showToast(getString(R.string.no_more_data))
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