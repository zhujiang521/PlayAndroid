package com.zj.play.view.main

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cpacm.FloatingMusicMenu
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zj.play.R
import com.zj.play.view.main.FragmentFactory.Companion.getCurrentFragment


class HomeBottomLandTabWidget @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mFragmentManager: FragmentManager? = null
    private var floatingButtons: ArrayList<FloatingActionButton>? = null
    private var mFragments: java.util.ArrayList<Fragment>? = null
    private var fabMenu: FloatingMusicMenu? = null
    private var mLastFgIndex = 0
    private lateinit var mViewModel: MainViewModel

    /**
     * 外部调用初始化，传入必要的参数
     *
     * @param fm
     */
    fun init(fm: FragmentManager?, viewModel: MainViewModel) {
        mFragmentManager = fm
        mViewModel = viewModel
        if (mFragments == null) {
            mFragments = arrayListOf()
            mFragments?.add(getCurrentFragment(0)!!)
            mFragments?.add(getCurrentFragment(1)!!)
            mFragments?.add(getCurrentFragment(2)!!)
            mFragments?.add(getCurrentFragment(3)!!)
        }
        fragmentManger(viewModel.getPage() ?: 0)
    }

    /**
     * 初始化 设置点击事件。
     *
     * @param view /
     */
    private fun initView(view: View) { //默认第一个碎片
        floatingButtons = arrayListOf(
            view.findViewById(R.id.fabHome),
            view.findViewById(R.id.fabRepo),
            view.findViewById(R.id.fabProject),
            view.findViewById(R.id.fabProfile)
        )
        fabMenu = view.findViewById(R.id.fabMenu)
        for (textView in floatingButtons!!) {
            textView.setOnClickListener(this)
        }
    }

    /**
     * 销毁，避免内存泄漏
     */
    fun destroy() {
        if (null != mFragmentManager) {
            if (!mFragmentManager!!.isDestroyed)
                mFragmentManager = null
        }
        if (!floatingButtons.isNullOrEmpty()) {
            floatingButtons!!.clear()
            floatingButtons = null
        }
        if (!mFragments.isNullOrEmpty()) {
            mFragments?.clear()
            mFragments = null
        }
    }

    /**
     * 实现按钮的点击事件
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabHome -> {
                fragmentManger(0)
            }
            R.id.fabRepo -> {
                fragmentManger(1)
            }
            R.id.fabProject -> {
                fragmentManger(2)
            }
            R.id.fabProfile -> {
                fragmentManger(3)
            }
        }
    }

    /**
     * fragment的切换 实现底部导航栏的切换
     *
     * @param position 序号
     */
    private fun fragmentManger(position: Int) {
        mViewModel.setPage(position)
        fabMenu?.setMusicCover(
            BitmapFactory.decodeResource(
                context.resources,
                when(position){
                    0 -> R.drawable.ic_nav_news_actived
                    1 -> R.drawable.ic_nav_tweet_actived
                    2 -> R.drawable.ic_nav_discover_actived
                    3 -> R.drawable.ic_nav_my_pressed
                    else -> R.drawable.ic_nav_news_actived
                }
            )
        )
        if (fabMenu != null && fabMenu!!.isExpanded)
            fabMenu!!.toggle()
        for (j in floatingButtons!!.indices) {
            floatingButtons!![j].isSelected = position == j
        }
        val fragmentTransaction =
            mFragmentManager!!.beginTransaction()

        val targetFg: Fragment = mFragments!![position]
        val lastFg: Fragment = mFragments!![mLastFgIndex]
        mLastFgIndex = position
        fragmentTransaction.hide(lastFg)
        if (!targetFg.isAdded) {
            mFragmentManager!!.beginTransaction().remove(targetFg)
                .commitAllowingStateLoss()
            fragmentTransaction.add(R.id.flHomeFragment, targetFg)
        }
        fragmentTransaction.show(targetFg)
        fragmentTransaction.commitAllowingStateLoss()
    }

    init {
        val view =
            View.inflate(context, R.layout.layout_home_bottom_land_tab, this)
        initView(view)
    }

}