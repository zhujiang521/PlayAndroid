package com.zj.play.view.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.zj.play.R
import com.zj.play.view.main.FragmentFactory.Companion.getCurrentFragment


class HomeBottomTabWidget @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mFragmentManager: FragmentManager? = null
    private var textViews: ArrayList<TextView>? = null
    private var mFragments: java.util.ArrayList<Fragment>? = null
    private var mLastFgIndex = 0

    /**
     * 外部调用初始化，传入必要的参数
     *
     * @param fm
     */
    fun init(fm: FragmentManager?) {
        mFragmentManager = fm
        if (mFragments == null) {
            mFragments = arrayListOf()
            mFragments?.add(getCurrentFragment(0)!!)
            mFragments?.add(getCurrentFragment(1)!!)
            mFragments?.add(getCurrentFragment(2)!!)
            mFragments?.add(getCurrentFragment(3)!!)
            fragmentManger(0)
        }
    }

    /**
     * 初始化 设置点击事件。
     *
     * @param view /
     */
    private fun initView(view: View) { //默认第一个碎片
        textViews = arrayListOf(
            view.findViewById(R.id.llHomeATHome)
            , view.findViewById(R.id.llHomeATCalendar)
            , view.findViewById(R.id.llHomeATObject)
            , view.findViewById(R.id.llHomeATMy)
        )
        for (textView in textViews!!) {
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
        if (!textViews.isNullOrEmpty()) {
            textViews!!.clear()
            textViews = null
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
            R.id.llHomeATHome -> fragmentManger(0)
            R.id.llHomeATCalendar -> fragmentManger(1)
            R.id.llHomeATObject -> fragmentManger(2)
            R.id.llHomeATMy -> fragmentManger(3)
        }
    }

    /**
     * fragment的切换 实现底部导航栏的切换
     *
     * @param position 序号
     */
    private fun fragmentManger(position: Int) {
        for (j in textViews!!.indices) {
            textViews!![j].isSelected = position == j
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
            View.inflate(context, R.layout.layout_home_bottom_tab, this)
        initView(view)
    }

}