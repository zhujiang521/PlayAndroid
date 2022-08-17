package com.zj.core.util

import android.util.Log

/**
 * Log 工具类
 */
object PlayLog {

    private const val APP_TAG = "PlayAndroid"

    @JvmStatic
    fun v(msg: String? = "") {
        Log.v(APP_TAG, "$msg")
    }

    @JvmStatic
    fun v(tag: String, msg: String? = "") {
        Log.v(tag, "$msg")
    }

    @JvmStatic
    fun d(msg: String? = "") {
        Log.d(APP_TAG, "$msg")
    }

    @JvmStatic
    fun d(tag: String, msg: String? = "") {
        Log.d(tag, "$msg")
    }

    @JvmStatic
    fun i(msg: String? = "") {
        Log.i(APP_TAG, "$msg")
    }

    @JvmStatic
    fun i(tag: String, msg: String? = "") {
        Log.i(tag, "$msg")
    }

    @JvmStatic
    fun w(msg: String? = "") {
        Log.w(APP_TAG, "$msg")
    }

    @JvmStatic
    fun w(tag: String, msg: String? = "") {
        Log.w(tag, "$msg")
    }

    @JvmStatic
    fun e(msg: String? = "") {
        Log.e(APP_TAG, "$msg")
    }

    @JvmStatic
    fun e(tag: String, msg: String? = "") {
        Log.e(tag, "$msg")
    }

    @JvmStatic
    fun e(msg: String? = "", tr: Throwable) {
        Log.e(APP_TAG, "$msg", tr)
    }

}