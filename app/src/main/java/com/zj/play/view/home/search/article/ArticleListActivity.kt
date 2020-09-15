package com.zj.play.view.home.search.article

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zj.play.R
import com.zj.play.view.article.ArticleAdapter
import com.zj.play.view.home.ArticleCollectBaseActivity
import kotlinx.android.synthetic.main.activity_article_list.*
import kotlin.system.measureTimeMillis

private const val KEYWORD = "KEYWORD"

class ArticleListActivity : ArticleCollectBaseActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(ArticleListViewModel::class.java) }

    private var keyword = ""
    private lateinit var articleAdapter: ArticleAdapter
    private var page = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_article_list
    }

    override fun initData() {
        keyword = intent.getStringExtra(KEYWORD) ?: ""
        articleListTitleBar.setTitle(keyword)
        viewModel.articleLiveData.observe(this, {
            if (it.isSuccess) {
                val articleList = it.getOrNull()
                if (articleList != null) {
                    loadFinished()
                    if (page == 1 && viewModel.articleList.size > 0) {
                        viewModel.articleList.clear()
                    }
                    viewModel.articleList.addAll(articleList.datas)
                    if (viewModel.articleList.size == 0) {
                        showNoContentView("没有关于 $keyword 的数据，请更换关键字搜索")
                    }
                    articleAdapter.notifyDataSetChanged()
                } else {
                    showLoadErrorView()
                }
            } else {
                showBadNetworkView { getArticleList() }
            }
        })
        getArticleList()
    }

    override fun initView() {
        artListRecycleView.layoutManager = LinearLayoutManager(this)
        articleAdapter = ArticleAdapter(
            this,
            R.layout.adapter_article,
            viewModel.articleList
        )
        articleAdapter.setHasStableIds(true)
        artListRecycleView.adapter = articleAdapter
        artListSmartRefreshLayout.apply {
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
        if (viewModel.articleList.size <= 0) startLoading()
        viewModel.getArticleList(page, keyword)
    }

    companion object {
        fun actionStart(context: Context, keyword: String) {
            val intent = Intent(context, ArticleListActivity::class.java).apply {
                putExtra(KEYWORD, keyword)
            }
            context.startActivity(intent)
        }
    }

}
