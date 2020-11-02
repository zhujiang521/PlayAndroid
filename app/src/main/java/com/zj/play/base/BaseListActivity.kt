package com.zj.play.base

import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zj.core.view.StaggeredDividerItemDecoration
import com.zj.play.R
import com.zj.play.home.ArticleCollectBaseActivity
import kotlinx.android.synthetic.main.activity_base_list.*
import kotlin.system.measureTimeMillis

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/10/20
 * 描述：PlayAndroid
 *
 */
abstract class BaseListActivity : ArticleCollectBaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_base_list
    }

    protected var page = 1

    override fun initData() {
        getDataList()
    }

    abstract fun getDataList()

    override fun initView() {
        when (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            true -> {
                baseListRecycleView.layoutManager = LinearLayoutManager(this)
            }
            false -> {
                val spanCount = 2
                if (isStaggeredGrid()) {
                    val layoutManager =
                        StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
                    baseListRecycleView.layoutManager = layoutManager
                    layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE;
                    baseListRecycleView.itemAnimator = null
                    baseListRecycleView.addItemDecoration(StaggeredDividerItemDecoration(this))
                    baseListRecycleView.addOnScrollListener(object :
                        RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(
                            recyclerView: RecyclerView,
                            newState: Int
                        ) {
                            val first = IntArray(spanCount)
                            layoutManager.findFirstCompletelyVisibleItemPositions(first)
                            if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                                layoutManager.invalidateSpanAssignments()
                            }
                        }
                    })
                } else {
                    baseListRecycleView.layoutManager = GridLayoutManager(this, spanCount)
                }
            }
        }
        baseListSmartRefreshLayout.apply {
            setOnRefreshListener { reLayout ->
                reLayout.finishRefresh(measureTimeMillis {
                    page = 1
                    getDataList()
                }.toInt())
            }
            setOnLoadMoreListener { reLayout ->
                val time = measureTimeMillis {
                    page++
                    getDataList()
                }.toInt()
                reLayout.finishLoadMore(if (time > 1000) time else 1000)
            }
        }
    }

    abstract fun isStaggeredGrid(): Boolean

}
