package com.zj.play.view.project

import androidx.lifecycle.ViewModel
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.zj.core.view.BaseFragment

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/12
 * 描述：PlayAndroid
 *
 */
abstract class BaseTabFragment : BaseFragment(), ViewPager.OnPageChangeListener,
    TabLayout.OnTabSelectedListener {

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }
}