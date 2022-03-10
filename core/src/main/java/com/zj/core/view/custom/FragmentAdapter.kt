package com.zj.core.view.custom

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020-01-18
 * 描述：pwqgc
 */
class FragmentAdapter(mFragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(
        mFragmentManager, lifecycle
    ) {
    private val mFragment: MutableList<Fragment> = ArrayList()
    private lateinit var mTitles: Array<String>

    fun reset(fragments: List<Fragment>?) {
        fragments?.apply {
            mFragment.clear()
            mFragment.addAll(this)
        }
    }

    fun title(position: Int): String {
        return mTitles[position]
    }

    fun reset(titles: Array<String>) {
        mTitles = titles
    }

    override fun getItemCount(): Int {
        return mFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragment[position]
    }
}