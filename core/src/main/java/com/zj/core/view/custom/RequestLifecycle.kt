package com.zj.core.view.custom

/**
 * 在Activity或Fragment中进行数据请求所需要经历的生命周期函数。
 *
 */
interface RequestLifecycle {

    fun startLoading()

    fun loadFinished()

    fun loadFailed(msg: String?)

}
