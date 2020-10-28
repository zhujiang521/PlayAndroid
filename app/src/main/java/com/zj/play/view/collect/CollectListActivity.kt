package com.zj.play.view.collect

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.zj.play.R
import com.zj.play.view.base.BaseListActivity
import kotlinx.android.synthetic.main.activity_base_list.*

class CollectListActivity : BaseListActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(CollectListViewModel::class.java) }

    private lateinit var articleAdapter: CollectAdapter

    override fun initData() {
        super.initData()
        baseListTitleBar.setTitle(getString(R.string.my_collection))
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
        viewModel.getDataList(page)
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, CollectListActivity::class.java)
            context.startActivity(intent)
        }
    }

}
