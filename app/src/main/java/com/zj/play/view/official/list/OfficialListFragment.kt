package com.zj.play.view.official.list

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.zj.play.view.article.ArticleAdapter
import com.zj.play.view.base.BaseListFragment

const val PROJECT_CID = "PROJECT_CID"

class OfficialListFragment : BaseListFragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(OfficialListViewModel::class.java) }

    private var projectCid: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectCid = it.getInt(PROJECT_CID)
        }
    }

    override fun refreshData() {
        getArticleList(true)
    }

    override fun isHaveHeadMargin(): Boolean {
        return false
    }

    override fun initView() {
        articleAdapter = ArticleAdapter(
            context!!,
            viewModel.articleList
        )
        super.initView()
    }

    private fun getArticleList(isRefresh: Boolean) {
        if (viewModel.articleList.size <= 0) startLoading()
        viewModel.getArticleList(page, projectCid!!, isRefresh)
    }

    override fun initData() {
        setDataStatus(viewModel.articleLiveData) {
            if (page == 1 && viewModel.articleList.size > 0) {
                viewModel.articleList.clear()
            }
            viewModel.articleList.addAll(it)
            articleAdapter.notifyDataSetChanged()
        }
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
