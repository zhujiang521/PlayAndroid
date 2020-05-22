package com.zj.play.view.official.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zj.core.view.BaseFragment
import com.zj.play.R
import com.zj.play.view.article.ArticleAdapter
import kotlinx.android.synthetic.main.fragment_official_list.*
import kotlin.system.measureTimeMillis

const val PROJECT_CID = "PROJECT_CID"

class OfficialListFragment : BaseFragment() {

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

    override fun getLayoutId(): Int {
        return R.layout.fragment_official_list
    }

    override fun initView() {
        offListRecycleView.layoutManager = LinearLayoutManager(context)
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
        viewModel.getArticleList(page, projectCid!!)
    }

    override fun initData() {
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
