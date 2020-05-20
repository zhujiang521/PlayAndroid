package com.zj.play.view.home

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.youth.banner.indicator.CircleIndicator
import com.zj.core.view.BaseFragment
import com.zj.play.R
import com.zj.play.network.Repository
import com.zj.play.view.article.ArticleAdapter
import com.zj.play.view.home.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_home_page.*
import kotlin.system.measureTimeMillis


class HomePageFragment : BaseFragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(HomePageViewModel::class.java) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_page
    }

    override fun onResume() {
        super.onResume()
        homeBanner.start()
    }

    private lateinit var bannerAdapter: ImageAdapter
    private lateinit var articleAdapter: ArticleAdapter
    private var page = 1

    override fun initView() {
        homeTitleBar.setTitle("首页")
        homeTitleBar.setRightText("搜索")
        homeTitleBar.setBackImageVisiable(false)
        homeTitleBar.setRightTextOnClickListener(View.OnClickListener {
            SearchActivity.actionStart(
                context!!
            )
        })
        bannerAdapter = ImageAdapter(context!!, viewModel.bannerList)
        homeBanner.adapter = bannerAdapter
        homeBanner.setIndicator(CircleIndicator(context))
            .start()
        homeRecycleView.layoutManager = LinearLayoutManager(context)
        articleAdapter = ArticleAdapter(
            context!!,
            R.layout.adapter_article,
            viewModel.articleList
        )
        articleAdapter.setHasStableIds(true)
        homeRecycleView.adapter = articleAdapter
        homeSmartRefreshLayout.apply {
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

    override fun initData() {
        initBanner()
        viewModel.articleLiveData.observe(this, Observer {
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
                showBadNetworkView(View.OnClickListener {
                    getArticleList()
                })
            }
        })
        getArticleList()
    }

    private fun initBanner() {
        Repository.getBanner().observe(this, Observer {
            val bannerList = it.getOrNull()
            if (bannerList != null) {
                viewModel.bannerList.addAll(bannerList)
                bannerAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun getArticleList() {
        startLoading()
        viewModel.getArticleList(page)
        if (page == 1) initBanner()
    }

    override fun onPause() {
        super.onPause()
        homeBanner.stop()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomePageFragment()
    }
}
