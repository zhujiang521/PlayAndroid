package com.zj.play.view.rank.list

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zj.play.model.RankData
import com.zj.play.view.base.BaseListActivity
import kotlinx.android.synthetic.main.activity_base_list.*

class RankActivity : BaseListActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(RankViewModel::class.java) }

    private lateinit var rankAdapter: RankAdapter

    override fun initData() {
        super.initData()
        setDataStatus(viewModel.dataLiveData)
    }

    override fun initView() {
        super.initView()
        baseListTitleBar.setTitle("排行榜")
        rankAdapter = RankAdapter(this, viewModel.dataList)
        rankAdapter.setHasStableIds(true)
        baseListRecycleView.adapter = rankAdapter
    }

    override fun isStaggeredGrid(): Boolean {
        return false
    }

    override fun getDataList() {
        if (viewModel.dataList.size <= 0) startLoading()
        viewModel.getDataList(page)
    }

    override fun <T> setData(articleList: T) {
        if (page == 1 && viewModel.dataList.size > 0) {
            viewModel.dataList.clear()
        }
        articleList as RankData
        viewModel.dataList.addAll(articleList.datas)
        rankAdapter.notifyDataSetChanged()
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, RankActivity::class.java)
            context.startActivity(intent)
        }
    }

}
