package com.zj.core.util

import android.content.Context
import android.view.View

/**
 * dip转换成pixel
 * @param context
 * @param dpValue
 * @return
 */
fun Context?.dp2px(dpValue: Float): Int {
    if (this == null) return dpValue.toInt()
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

inline fun View.setSafeListener(crossinline action: () -> Unit) {
    var lastClick = 0L
    setOnClickListener {
        val gap = System.currentTimeMillis() - lastClick
        if (gap < 800) return@setOnClickListener
        action.invoke()
        lastClick = System.currentTimeMillis()
    }
}