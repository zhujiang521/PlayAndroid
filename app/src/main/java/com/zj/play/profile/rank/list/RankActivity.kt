package com.zj.play.profile.rank.list

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.zj.play.R
import com.zj.play.base.BaseListActivity
import com.zj.play.main.startNewActivity
import kotlinx.android.synthetic.main.activity_base_list.*

class RankActivity : BaseListActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(RankViewModel::class.java) }

    private lateinit var rankAdapter: RankAdapter

    override fun initData() {
        super.initData()
        setDataStatus(viewModel.dataLiveData) {
            if (page == 1 && viewModel.dataList.size > 0) {
                viewModel.dataList.clear()
            }
            viewModel.dataList.addAll(it.datas)
            rankAdapter.notifyDataSetChanged()
        }
    }

    override fun initView() {
        super.initView()
        baseListTitleBar.setTitle(getString(R.string.ranking_list))
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

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, RankActivity::class.java)
            context.startNewActivity(intent)
        }
    }

}
