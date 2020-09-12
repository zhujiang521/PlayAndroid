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


abstract class BaseHomeBottomTabWidget @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    layoutId:Int
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mFragmentManager: FragmentManager? = null
    private var mFragments: java.util.ArrayList<Fragment>? = null
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
    @Suppress("LeakingThis")
    abstract fun initView(view: View)

    /**
     * 销毁，避免内存泄漏
     */
    open fun destroy() {
        if (null != mFragmentManager) {
            if (!mFragmentManager!!.isDestroyed)
                mFragmentManager = null
        }
        if (!mFragments.isNullOrEmpty()) {
            mFragments?.clear()
            mFragments = null
        }
    }

    /**
     * fragment的切换 实现底部导航栏的切换
     *
     * @param position 序号
     */
    protected open fun fragmentManger(position: Int) {
        mViewModel.setPage(position)
        val fragmentTransaction =
            mFragmentManager!!.beginTransaction()
        val targetFg: Fragment = mFragments!![position]
        val lastFg: Fragment = mFragments!![mLastFgIndex]
        mLastFgIndex = position
        if (lastFg.isAdded)
            fragmentTransaction.hide(lastFg)
        if (!targetFg.isAdded) {
            mFragmentManager!!.beginTransaction().remove(targetFg)
                .commitAllowingStateLoss()
            fragmentTransaction.add(R.id.flHomeFragment, targetFg)
        }
        fragmentTransaction.show(targetFg)
        fragmentTransaction.commit()
    }

    init {
        initView(View.inflate(context, layoutId, this))
    }

}