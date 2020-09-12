package com.zj.play.view.rank.user

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zj.core.view.BaseActivity
import com.zj.play.R
import kotlinx.android.synthetic.main.activity_user_rank.*
import kotlin.system.measureTimeMillis

class UserRankActivity : BaseActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(UserRankViewModel::class.java) }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_rank
    }

    private lateinit var rankAdapter: UserRankAdapter
    private var page = 1

    override fun initData() {
        viewModel.rankLiveData.observe(this, Observer {
            if (it.isSuccess) {
                val articleList = it.getOrNull()
                if (articleList != null) {
                    loadFinished()
                    if (page == 1 && viewModel.rankList.size > 0) {
                        viewModel.rankList.clear()
                    }
                    viewModel.rankList.addAll(articleList.datas)
                    rankAdapter.notifyDataSetChanged()
                } else {
                    showLoadErrorView()
                }
            } else {
                showBadNetworkView { getRankList() }
            }
        })
        getRankList()
    }

    override fun initView() {
        userRankTitleBar.setTitle("排行榜")
        userRankListRecycleView.layoutManager = LinearLayoutManager(this)
        rankAdapter = UserRankAdapter(this, R.layout.adapter_rank, viewModel.rankList)
        rankAdapter.setHasStableIds(true)
        userRankListRecycleView.adapter = rankAdapter
        userRankListSmartRefreshLayout.apply {
            setOnRefreshListener { reLayout ->
                reLayout.finishRefresh(measureTimeMillis {
                    page = 1
                    getRankList()
                }.toInt())
            }
            setOnLoadMoreListener { reLayout ->
                val time = measureTimeMillis {
                    page++
                    getRankList()
                }.toInt()
                reLayout.finishLoadMore(if (time > 1000) time else 1000)
            }
        }
    }

    private fun getRankList() {
        if (viewModel.rankList.size <= 0) startLoading()
        viewModel.getRankList(page)
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, UserRankActivity::class.java)
            context.startActivity(intent)
        }
    }

}
