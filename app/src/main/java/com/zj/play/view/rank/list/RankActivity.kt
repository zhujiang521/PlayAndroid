package com.zj.play.view.rank.list

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zj.core.view.BaseActivity
import com.zj.play.R
import kotlinx.android.synthetic.main.activity_rank.*
import kotlin.system.measureTimeMillis

class RankActivity : BaseActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(RankViewModel::class.java) }

    override fun getLayoutId(): Int {
        return R.layout.activity_rank
    }

    private lateinit var rankAdapter: RankAdapter
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
                showBadNetworkView(View.OnClickListener {
                    getRankList()
                })
            }
        })
        getRankList()
    }

    override fun initView() {
        rankTitleBar.setTitle("排行榜")
        rankListRecycleView.layoutManager = LinearLayoutManager(this)
        rankAdapter = RankAdapter(
            this,
            R.layout.adapter_rank,
            viewModel.rankList
        )
        rankAdapter.setHasStableIds(true)
        rankListRecycleView.adapter = rankAdapter
        rankListSmartRefreshLayout.apply {
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
        startLoading()
        viewModel.getRankList(page)
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, RankActivity::class.java)
            context.startActivity(intent)
        }
    }

}
