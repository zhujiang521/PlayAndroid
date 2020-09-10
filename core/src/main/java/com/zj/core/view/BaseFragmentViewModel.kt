package com.zj.core.view

import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModel

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
class BaseFragmentViewModel : ViewModel()  {


    /**
     * Fragment中由于服务器异常导致加载失败显示的布局。
     */
    var loadErrorView: RelativeLayout? = null

    /**
     * Fragment中由于网络异常导致加载失败显示的布局。
     */
    var badNetworkView: RelativeLayout? = null

    /**
     * Fragment中当界面上没有任何内容时展示的布局。
     */
    var noContentView: RelativeLayout? = null

    /**
     * Fragment中inflate出来的布局。
     */
    var rootView: View? = null

    /**
     * Fragment中显示加载等待的控件。
     */
    var loading: ProgressBar? = null

}