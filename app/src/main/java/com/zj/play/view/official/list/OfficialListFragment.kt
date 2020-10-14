package com.zj.play.view.official.list

import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zj.core.util.showToast
import com.zj.play.R
import com.zj.play.view.article.ArticleAdapter
import com.zj.play.view.home.ArticleCollectBaseFragment
import kotlinx.android.synthetic.main.fragment_official_list.*
import kotlin.system.measureTimeMillis

const val PROJECT_CID = "PROJECT_CID"

class OfficialListFragment : ArticleCollectBaseFragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(OfficialListViewModel::class.java) }

    private var projectCid: Int? = null
    private lateinit var articleAdapter: ArticleAdapter
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectCid = it.getInt(PROJECT_CID)
        }
    }

    override fun refreshData() {
        getArticleList(true)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_official_list
    }

    override fun initView() {
        when (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            true -> {
                offListRecycleView.layoutManager = LinearLayoutManager(context)
            }
            false -> {
                val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                offListRecycleView.layoutManager = layoutManager
            }
        }
        articleAdapter = ArticleAdapter(
            context!!,
            R.layout.adapter_article,
            viewModel.articleList
        )
        articleAdapter.setHasStableIds(true)
        offListRecycleView.adapter = articleAdapter
        offListSmartRefreshLayout.apply {
            setOnRefreshListener { reLayout ->
                reLayout.finishRefresh(measureTimeMillis {
                    page = 1
                    getArticleList(true)
                }.toInt())
            }
            setOnLoadMoreListener { reLayout ->
                val time = measureTimeMillis {
                    page++
                    getArticleList(true)
                }.toInt()
                reLayout.finishLoadMore(if (time > 1000) time else 1000)
            }
        }
    }

    private fun getArticleList(isRefresh: Boolean) {
        if (viewModel.articleList.size <= 0) startLoading()
        viewModel.getArticleList(page, projectCid!!, isRefresh)
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
                if (viewModel.articleList.size > 0) {
                    showToast("网络请求出错")
                } else {
                    showBadNetworkView { getArticleList(true) }
                }
            }
        })
        getArticleList(false)
    }

    companion object {
        @JvmStatic
        fun newInstance(cid: Int) =
            OfficialListFragment().apply {
                arguments = Bundle().apply {
                    putInt(PROJECT_CID, cid)
                }
            }
    }

}
