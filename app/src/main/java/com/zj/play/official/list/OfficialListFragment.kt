package com.zj.play.official.list

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.zj.model.pojo.QueryArticle
import com.zj.play.article.ArticleAdapter
import com.zj.play.base.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

const val PROJECT_CID = "PROJECT_CID"

@AndroidEntryPoint
class OfficialListFragment : BaseListFragment() {

    private val viewModel by viewModels<OfficialListViewModel>()

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
        articleAdapter = ArticleAdapter(requireContext(), viewModel.dataList)
        super.initView()
    }

    private fun getArticleList(isRefresh: Boolean) {
        if (viewModel.dataList.size <= 0) startLoading()
        projectCid?.apply {
            viewModel.getDataList(QueryArticle(page, this, isRefresh))
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        setDataStatus(viewModel.dataLiveData, {
            if (viewModel.dataList.size > 0) loadFinished()
        }) {
            if (page == 1 && viewModel.dataList.size > 0) {
                viewModel.dataList.clear()
            }
            viewModel.dataList.addAll(it)
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
