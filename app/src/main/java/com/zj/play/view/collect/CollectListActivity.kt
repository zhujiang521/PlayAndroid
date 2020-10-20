package com.zj.play.view.collect

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zj.play.view.base.BaseListActivity
import com.zj.core.view.StaggeredDividerItemDecoration
import kotlinx.android.synthetic.main.activity_base_list.*

class CollectListActivity : BaseListActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(CollectListViewModel::class.java) }

    private lateinit var articleAdapter: CollectAdapter

    override fun initData() {
        super.initData()
        baseListTitleBar.setTitle("我的收藏")
        viewModel.dataLiveData.observe(this, {
            if (it.isSuccess) {
                val articleList = it.getOrNull()
                if (articleList != null) {
                    loadFinished()
                    if (page == 1 && viewModel.dataList.size > 0) {
                        viewModel.dataList.clear()
                    }
                    viewModel.dataList.addAll(articleList.datas)
                    articleAdapter.notifyDataSetChanged()
                } else {
                    showLoadErrorView()
                }
            } else {
                showBadNetworkView { getDataList() }
            }
        })
    }

    override fun initView() {
        super.initView()
        articleAdapter = CollectAdapter(
            this,
            viewModel.dataList
        )
        articleAdapter.setHasStableIds(true)
        baseListRecycleView.adapter = articleAdapter
    }

    override fun isStaggeredGrid(): Boolean {
        return true
    }

    override fun getDataList() {
        if (viewModel.dataList.size <= 0) startLoading()
        viewModel.getDataList(page)
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, CollectListActivity::class.java)
            context.startActivity(intent)
        }
    }

}
