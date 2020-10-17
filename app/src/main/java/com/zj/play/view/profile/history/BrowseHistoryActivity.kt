package com.zj.play.view.profile.history

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zj.core.util.showToast
import com.zj.core.view.StaggeredDividerItemDecoration
import com.zj.play.R
import com.zj.play.view.article.ArticleAdapter
import com.zj.play.view.home.ArticleCollectBaseActivity
import kotlinx.android.synthetic.main.activity_browse_history.*
import kotlinx.android.synthetic.main.activity_collect_list.*
import kotlin.system.measureTimeMillis

class BrowseHistoryActivity : ArticleCollectBaseActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(BrowseHistoryViewModel::class.java) }
    private lateinit var articleAdapter: ArticleAdapter
    private var page = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_browse_history
    }

    override fun initView() {
        when (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            true -> {
                historyRecycleView.layoutManager = LinearLayoutManager(this)
            }
            false -> {
                val spanCount = 2
                val layoutManager =
                    StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
                historyRecycleView.layoutManager = layoutManager
                layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE;
                historyRecycleView.itemAnimator = null
                historyRecycleView.addItemDecoration(StaggeredDividerItemDecoration(this))
                historyRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        val first = IntArray(spanCount)
                        layoutManager.findFirstCompletelyVisibleItemPositions(first)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                            layoutManager.invalidateSpanAssignments()
                        }
                    }
                })
            }
        }
        articleAdapter = ArticleAdapter(
            this,
            viewModel.articleList,
            false
        )
        articleAdapter.setHasStableIds(true)
        historyRecycleView.adapter = articleAdapter
        historySmartRefreshLayout.apply {
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
        if (viewModel.articleList.size <= 0) {
            startLoading()
        }
        viewModel.getArticleList(page)
    }

    override fun initData() {
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
        getArticleList()
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, BrowseHistoryActivity::class.java)
            context.startActivity(intent)
        }
    }

}