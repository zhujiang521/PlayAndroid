package com.zj.core.util

import android.view.View

/**
 * 版权：Zhujiang 个人版权
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