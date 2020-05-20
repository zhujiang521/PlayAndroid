package com.zj.play.view.collect

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zj.core.view.BaseActivity
import com.zj.play.R
import com.zj.play.view.article.ArticleAdapter
import kotlinx.android.synthetic.main.activity_collect_list.*
import kotlin.system.measureTimeMillis

class CollectListActivity : BaseActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(CollectListViewModel::class.java) }

    override fun getLayoutId(): Int {
        return R.layout.activity_collect_list
    }

    private lateinit var articleAdapter: CollectAdapter
    private var page = 1

    override fun initData() {
        collectTitleBar.setTitle("我的收藏")
        viewModel.collectLiveData.observe(this, Observer {
            if (it.isSuccess) {
                val articleList = it.getOrNull()
                if (articleList != null) {
                    loadFinished()
                    if (page == 1 && viewModel.collectList.size > 0) {
                        viewModel.collectList.clear()
                    }
                    viewModel.collectList.addAll(articleList.datas)
                    articleAdapter.notifyDataSetChanged()
                } else {
                    showLoadErrorView()
                }
            } else {
                showBadNetworkView(View.OnClickListener {
                    getArticleList()
                })
            }
        })
        getArticleList()
    }

    override fun initView() {
        collectRecycleView.layoutManager = LinearLayoutManager(this)
        articleAdapter = CollectAdapter(
            this,
            R.layout.adapter_article,
            viewModel.collectList
        )
        articleAdapter.setHasStableIds(true)
        collectRecycleView.adapter = articleAdapter
        collectSmartRefreshLayout.apply {
            setOnRefreshListener { reLayout ->
                reLayout.finishRefresh(measureTimeMillis {
                    page = 1
                    getArticleList()
                }.toInt())
            }
            setOnLoadMoreListener { reLayout ->
                val time = measureTimeMillis {
                    page++
                    getArticleList()
                }.toInt()
                reLayout.finishLoadMore(if (time > 1000) time else 1000)
            }
        }
    }

    private fun getArticleList() {
        startLoading()
        viewModel.getArticleList(page)
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, CollectListActivity::class.java)
            context.startActivity(intent)
        }
    }

}
