package com.zj.play.profile.rank.user

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.zj.play.R
import com.zj.play.base.BaseListActivity
import kotlinx.android.synthetic.main.activity_base_list.*

class UserRankActivity : BaseListActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(UserRankViewModel::class.java) }

    private lateinit var rankAdapter: UserRankAdapter

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
        rankAdapter = UserRankAdapter(this, R.layout.adapter_rank, viewModel.dataList)
        baseListToTop.setAdapter(rankAdapter)
        baseListTitleBar.setTitle(getString(R.string.mine_points))
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
            val intent = Intent(context, UserRankActivity::class.java)
            context.startActivity(intent)
        }
    }

}
