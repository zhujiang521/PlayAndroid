package com.zj.play.base

import android.content.res.Configuration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zj.core.view.custom.StaggeredDividerItemDecoration
import com.zj.play.R
import com.zj.play.article.ArticleAdapter
import com.zj.play.home.ArticleCollectBaseFragment
import kotlinx.android.synthetic.main.fragment_base_list.*
import kotlin.system.measureTimeMillis

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/10/20
 * 描述：PlayAndroid
 *
 */
abstract class BaseListFragment : ArticleCollectBaseFragment() {

    protected lateinit var articleAdapter: ArticleAdapter
    protected var page = 1

    override fun getLayoutId(): Int = R.layout.fragment_base_list

    override fun initView() {
        when (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            true -> {
                baseFragmentListRecycleView.layoutManager = LinearLayoutManager(requireContext())
            }
            false -> {
                val spanCount = 2
                val layoutManager =
                    StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
                baseFragmentListRecycleView.layoutManager = layoutManager
                layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE;
                baseFragmentListRecycleView.itemAnimator = null
                baseFragmentListRecycleView.addItemDecoration(
                    StaggeredDividerItemDecoration(
                        requireContext()
                    )
                )
                baseFragmentListRecycleView.addOnScrollListener(object :
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
            }
        }
        articleAdapter.setHasStableIds(true)
        baseFragmentListRecycleView.adapter = articleAdapter
        baseFragmentListSmartRefreshLayout.apply {
            setOnRefreshListener { reLayout ->
                reLayout.finishRefresh(measureTimeMillis {
                    page = 1
                    refreshData()
                }.toInt())
            }
            setOnLoadMoreListener { reLayout ->
                val time = measureTimeMillis {
                    page++
                    refreshData()
                }.toInt()
                reLayout.finishLoadMore(if (time > 1000) time else 1000)
            }
        }
    }

}
