package com.zj.play.article.collect

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.zj.play.R
import com.zj.play.base.BaseListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectListActivity : BaseListActivity() {

    private val viewModel by viewModels<CollectListViewModel>()

    private lateinit var articleAdapter: CollectAdapter

    override fun initData() {
        super.initData()
        binding.baseListTitleBar.setTitle(getString(R.string.my_collection))
        setDataStatus(viewModel.dataLiveData) {
            if (page == 1 && viewModel.dataList.size > 0) {
                viewModel.dataList.clear()
            }
            viewModel.dataList.addAll(it.datas)
            articleAdapter.notifyDataSetChanged()
        }
    }

    override fun initView() {
        super.initView()
        articleAdapter = CollectAdapter(
            this,
            viewModel.dataList,
            lifecycleScope
        )
        binding.baseListToTop.setAdapter(articleAdapter)
    }

    override fun isStaggeredGrid(): Boolean {
        return true
    }

    override fun getDataList() {
        if (viewModel.dataList.size <= 0) startLoading()
        viewModel.getDataList(page)
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, CollectListActivity::class.java)
            context.startActivity(intent)
        }
    }

}
