package com.zj.play.profile.rank.list

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.zj.play.R
import com.zj.play.base.BaseListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankActivity : BaseListActivity() {

    private val viewModel by viewModels<RankViewModel>()

    private lateinit var rankAdapter: RankAdapter

    override fun initData() {
        super.initData()
        setDataStatus(viewModel.dataLiveData) {
            if (page == 1 && viewModel.dataList.size > 0) {
                viewModel.dataList.clear()
            }
            viewModel.dataList.addAll(it.datas)
            rankAdapter.notifyItemInserted(it.datas.size)
        }
    }

    override fun initView() {
        super.initView()
        binding.baseListTitleBar.setTitle(getString(R.string.ranking_list))
        rankAdapter = RankAdapter(this, viewModel.dataList)
        binding.baseListToTop.setAdapter(rankAdapter)
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
            context.startActivity(intent)
        }
    }

}
