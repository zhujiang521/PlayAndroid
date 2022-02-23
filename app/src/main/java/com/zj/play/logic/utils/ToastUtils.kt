package com.zj.play.logic.utils

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import com.zj.play.App

private var mToast: Toast? = null

/**
 * 显示时间较短的吐司
 *
 * @param text String，显示的内容
 */
fun showToast(text: String?) {
    val context = App.context
    if (context == null || TextUtils.isEmpty(text)) return
    if (Thread.currentThread() === Looper.getMainLooper().thread) {
        showToast(text, Toast.LENGTH_SHORT)
    } else {
        Handler(context.mainLooper).post { showToast(text, Toast.LENGTH_SHORT) }
    }
}

/**
 * 显示时间较短的吐司
 *
 * @param resId int，显示内容的字符串索引
 */
fun showToast(resId: Int) {
    val context = App.context ?: return
    if (Thread.currentThread() === Looper.getMainLooper().thread) {
        showToast(resId, Toast.LENGTH_SHORT)
    } else {
        Handler(context.mainLooper).post { showToast(resId, Toast.LENGTH_SHORT) }
    }
}

/**
 * 显示时间较长的吐司
 *
 * @param text String，显示的内容
 */
fun showLongToast(text: String?) {
    val context = App.context
    if (context == null || TextUtils.isEmpty(text)) return
    if (Thread.currentThread() === Looper.getMainLooper().thread) {
        showToast(text, Toast.LENGTH_LONG)
    } else {
        Handler(context.mainLooper).post { showToast(text, Toast.LENGTH_LONG) }
    }
}

/**
 * 显示时间较长的吐司
 *
 * @param resId int，显示内容的字符串索引
 */
fun showLongToast(resId: Int) {
    val context = App.context ?: return
    if (Thread.currentThread() === Looper.getMainLooper().thread) {
        showToast(resId, Toast.LENGTH_LONG)
    } else {
        Handler(context.mainLooper).post { showToast(resId, Toast.LENGTH_LONG) }
    }
}

private fun showToast(text: String?, duration: Int) {
    val context = App.context ?: return
    if (TextUtils.isEmpty(text)) return
    cancelToast()
    if (mToast == null) {
        mToast = Toast.makeText(context, null as CharSequence?, duration)
    }
    mToast?.apply {
        setText(text)
        this.duration = duration
        show()
    }
}

fun showToast(res: Int, duration: Int) {
    val context = App.context
    cancelToast()
    if (mToast == null) {
        mToast = Toast.makeText(context, res, duration)
    } else {
        mToast?.setText(res)
        mToast?.duration = duration
    }
    mToast?.show()
}

fun cancelToast() {
    mToast?.cancel()
}