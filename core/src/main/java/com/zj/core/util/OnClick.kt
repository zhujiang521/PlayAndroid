package com.zj.core.util

import android.view.View

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/4/29
 * 描述：AndroidAOP
 *
 */


inline fun View.setSafeListener(crossinline action: () -> Unit) {
    var lastClick = 0L
    setOnClickListener {
        val gap = System.currentTimeMillis() - lastClick
        lastClick = System.currentTimeMillis()
        if (gap < 1500) return@setOnClickListener
        action.invoke()
    }
}


var _viewClickFlag = false
var _clickRunnable = Runnable { _viewClickFlag = false }
fun View.click(action: (view: View) -> Unit) {
    setOnClickListener {
        if (!_viewClickFlag) {
            _viewClickFlag = true
            action(it)
        }
        removeCallbacks(_clickRunnable)
        postDelayed(_clickRunnable, 1000)
    }
}