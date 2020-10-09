package com.zj.core.util

import com.blankj.utilcode.util.ToastUtils

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/15
 * 描述：PlayAndroid
 *
 */

fun showToast(msg: String) {
    ToastUtils.showShort(msg)
}

fun showLongToast(msg: String) {
    ToastUtils.showLong(msg)
}