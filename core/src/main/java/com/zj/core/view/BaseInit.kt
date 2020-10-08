package com.zj.core.view

/**
 * 在Activity或Fragment中初始化需要的函数。
 *
 */
interface BaseInit {

    fun initData()

    fun initView()

    fun getLayoutId(): Int

}
