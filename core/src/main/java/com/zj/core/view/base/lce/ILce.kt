package com.zj.core.view.base.lce

import android.view.View
import com.zj.core.Play
import com.zj.core.R

/**
 * 策略模式
 *
 * @author zhujiang
 */
interface ILce {

    fun startLoading()

    fun loadFinished()

    /**
     * 当Activity中的加载内容服务器返回失败，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    fun showLoadErrorView(tip: String = Play.context!!.getString(R.string.failed_load_data))

    /**
     * 当Activity中的内容因为网络原因无法显示的时候，通过此方法显示提示界面给用户。
     *
     * @param listener
     * 重新加载点击事件回调
     */
    fun showBadNetworkView(listener: View.OnClickListener)

    /**
     * 当Activity中没有任何内容的时候，通过此方法显示提示界面给用户。
     * @param tip
     * 界面中的提示信息
     */
    fun showNoContentView(tip: String)

}