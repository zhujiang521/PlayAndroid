package com.zj.play.view.home

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.youth.banner.indicator.CircleIndicator
import com.zj.core.util.showToast
import com.zj.play.R
import com.zj.play.view.article.ArticleAdapter
import com.zj.play.view.home.search.SearchActivity
import com.zj.play.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_home_page.*
import kotlin.system.measureTimeMillis


class HomePageFragment : ArticleCollectBaseFragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(HomePageViewModel::class.java) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_page
    }

    override fun onResume() {
        super.onResume()
        homeBanner.start()
    }

    override fun refreshData() {
        getArticleList(true)
    }

    private lateinit var bannerAdapter: ImageAdapter
    private lateinit var bannerAdapter2: ImageAdapter
    private lateinit var articleAdapter: ArticleAdapter
    private var page = 1

    override fun initView() {
        homeTitleBar.setRightText("搜索")
        homeTitleBar.setRightTextOnClickListener {
            SearchActivity.actionStart(context!!)
        }
        bannerAdapter = ImageAdapter(context!!, viewModel.bannerList)
        bannerAdapter2 = ImageAdapter(context!!, viewModel.bannerList2)
        homeBanner.adapter = bannerAdapter
        homeBanner.setIndicator(CircleIndicator(context))
            .start()
        homeBanner2.adapter = bannerAdapter2
        homeBanner2.setIndicator(CircleIndicator(context))
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

    override fun initData() {
        startLoading()
        initBanner()
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
                    showBadNetworkView { initData() }
                }
            }
        })
        getArticleList(false)
    }

    private fun initBanner() {
        viewModel.bannerLiveData.observe(this, {
            if (it.isSuccess) {
                val bannerList = it.getOrNull()
                if (bannerList != null) {
                    val main = activity as MainActivity
                    if (viewModel.bannerList.size > 0)
                        viewModel.bannerList.clear()
                    if (viewModel.bannerList2.size > 0)
                        viewModel.bannerList2.clear()
                    if (main.isPort) {
                        viewModel.bannerList.addAll(bannerList)
                    } else {
                        for (index in bannerList.indices) {
                            if (index / 2 == 0) {
                                viewModel.bannerList.add(bannerList[index])
                            } else {
                                viewModel.bannerList2.add(bannerList[index])
                            }
                        }
                    }
                    bannerAdapter.notifyDataSetChanged()
                    bannerAdapter2.notifyDataSetChanged()
                }
            } else {
                showBadNetworkView { initData() }
            }
        })
        getBanner()
    }

    private fun getBanner() {
        viewModel.getBanner(false)
    }

    private fun getArticleList(isRefresh: Boolean) {
        viewModel.getArticleList(page, isRefresh)
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
