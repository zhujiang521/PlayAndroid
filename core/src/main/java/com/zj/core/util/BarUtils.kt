package com.zj.core.util

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.fragment.app.Fragment


/**
 * 设置透明状态栏
 */
fun Activity.transparentStatusBar() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

fun Context?.getStatusBarHeight(): Int {
    var result = 60
    if (this == null) return result
    val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resId > 0) {
        result = resources.getDimensionPixelOffset(resId)
    }
    return result
}


/**
 * 状态栏反色
 */
fun Activity.setAndroidNativeLightStatusBar(isDarkMode: Boolean) {
    val controller = WindowCompat.getInsetsController(window, window.decorView)
    controller.isAppearanceLightStatusBars = isDarkMode
}

fun Fragment?.showIme(currentFocusView: View? = null) {
    this?.activity?.showIme(currentFocusView)
}

fun Fragment?.hideIme(currentFocusView: View? = null) {
    this?.activity?.hideIme(currentFocusView)
}

/**
 * 显示ime
 */
fun Activity?.showIme(currentFocusView: View? = null) {
    if (this == null || window == null) return
    val view = currentFocusView ?: window.decorView
    view.isFocusable = true
    view.requestFocus()
    view.findFocus()
    val controller = WindowCompat.getInsetsController(window, view)
    Handler(Looper.getMainLooper()).postDelayed({
        controller.show(ime())
    }, 100L)
}

/**
 * 隐藏ime
 */
fun Activity?.hideIme(currentFocusView: View? = null) {
    if (this == null || window == null) return
    val view = currentFocusView ?: window.decorView
    view.clearFocus()
    val controller = WindowCompat.getInsetsController(window, view)
    controller.hide(ime())
}

fun Activity?.hideKeyboard(currentFocusView: View? = null) {
    if (this == null || window == null) return
    val view = currentFocusView ?: window.decorView
    prvHideKeyboard(view)
}

fun Fragment?.hideKeyboard(currentFocusView: View? = null) {
    this?.activity?.hideKeyboard(currentFocusView)
}

private fun Context.prvHideKeyboard(view: View) {
    view.clearFocus()
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}