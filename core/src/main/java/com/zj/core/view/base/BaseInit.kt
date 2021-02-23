package com.zj.core.view.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 在Activity或Fragment中初始化需要的函数。
 *
 */
interface BaseInit {

    fun initData()

    fun initView()

}

interface BaseActivityInit : BaseInit {

    fun getLayoutView(): View

}

interface BaseFragmentInit : BaseInit {

    fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): View

}