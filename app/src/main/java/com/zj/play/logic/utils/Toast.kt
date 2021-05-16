package com.zj.play.logic.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/15
 * 描述：PlayAndroid
 *
 */

fun showToast(context: Context?, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context?, @StringRes msgId: Int) {
    Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show()
}

fun showLongToast(context: Context?, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}

fun showLongToast(context: Context?, @StringRes msgId: Int) {
    Toast.makeText(context, msgId, Toast.LENGTH_LONG).show()
}