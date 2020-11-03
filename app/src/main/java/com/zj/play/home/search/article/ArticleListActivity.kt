package com.zj.play.home.search.article

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.zj.play.R
import com.zj.play.article.ArticleAdapter
import com.zj.play.base.BaseListActivity
import com.zj.play.main.startNewActivity
import kotlinx.android.synthetic.main.activity_base_list.*

private const val KEYWORD = "KEYWORD"

class ArticleListActivity : BaseListActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(ArticleListViewModel::class.java) }

    private var keyword = ""
    private lateinit var articleAdapter: ArticleAdapter

    override fun initData() {
        keyword = intent.getStringExtra(KEYWORD) ?: ""
        super.initData()
        baseListTitleBar.setTitle(keyword)
        setDataStatus(viewModel.dataLiveData) {
            if (page == 1 && viewModel.dataList.size > 0) {
                viewModel.dataList.clear()
            }
            viewModel.dataList.addAll(it.datas)
            if (viewModel.dataList.size == 0) {
                showNoContentView(getString(R.string.keyword_null, keyword))
            }
            articleAdapter.notifyDataSetChanged()
        }
    }

    override fun initView() {
        super.initView()
        articleAdapter = ArticleAdapter(
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
        viewModel.getDataList(QueryKeyArticle(page, keyword))
    }

    companion object {
        fun actionStart(context: Context, keyword: String) {
            val intent = Intent(context, ArticleListActivity::class.java).apply {
                putExtra(KEYWORD, keyword)
            }
            context.startNewActivity(intent)
        }
    }

}
