package com.zj.core.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zj.core.R
import com.zj.core.view.base.BaseListAdapter
import kotlin.system.measureTimeMillis

/**
 * 自定义头部View
 *
 * @author jiang zhu on 2019/10/7
 */
class ToTopRecyclerView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(mContext, attrs, defStyleAttr), View.OnClickListener {
    private lateinit var mToTopSmartRefreshLayout: SmartRefreshLayout
    private lateinit var mToTopRecycleView: RecyclerView
    private lateinit var mToTopIvClick: ImageView
    private val mLoadTime = 1000

    /**
     * 初始化布局
     */
    private fun initView() {
        //加载布局
        View.inflate(mContext, R.layout.layout_to_top, this)

        mToTopSmartRefreshLayout = findViewById(R.id.toTopSmartRefreshLayout)
        mToTopRecycleView = findViewById(R.id.toTopRecycleView)
        mToTopIvClick = findViewById(R.id.toTopIvClick)
        mToTopIvClick.setOnClickListener(this)
        mToTopRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(-1)) {
                    mToTopIvClick.visibility = View.GONE
                } else if (dy < 0) {
                    mToTopIvClick.visibility = View.VISIBLE
                } else if (dy > 0) {
                    mToTopIvClick.visibility = View.GONE
                }
            }
        })
    }

    fun setAdapter(adapter: RecyclerView.Adapter<BaseListAdapter.ViewHolder>) {
        adapter.setHasStableIds(true)
        mToTopRecycleView.adapter = adapter
    }

    fun onRefreshListener(onRefreshListener: () -> Unit, onLoadMoreListener: () -> Unit) {
        mToTopSmartRefreshLayout.apply {
            setOnRefreshListener { reLayout ->
                reLayout.finishRefresh(measureTimeMillis {
                    onRefreshListener.invoke()
                }.toInt())
            }
            setOnLoadMoreListener { reLayout ->
                val time = measureTimeMillis {
                    onLoadMoreListener.invoke()
                }.toInt()
                reLayout.finishLoadMore(if (time > mLoadTime) time else mLoadTime)
            }
        }
    }

    fun setRecyclerViewLayoutManager(isLinearLayout: Boolean) {
        if (isLinearLayout) {
            mToTopRecycleView.layoutManager = LinearLayoutManager(context)
        } else {
            val spanCount = 2
            val layoutManager =
                StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
            mToTopRecycleView.layoutManager = layoutManager
            layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            mToTopRecycleView.itemAnimator = null
            mToTopRecycleView.addItemDecoration(
                StaggeredDividerItemDecoration(
                    context
                )
            )
            mToTopRecycleView.addOnScrollListener(object :
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


    init {
        initView()
    }

    override fun onClick(v: View) {
        mToTopRecycleView.smoothScrollToPosition(0)
    }
}