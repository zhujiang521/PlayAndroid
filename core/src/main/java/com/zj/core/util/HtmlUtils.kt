package com.zj.core.util

import android.text.Html

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：11/16/20
 * 描述：PlayAndroid
 *
 */

fun getHtmlText(text: String): String {
    return if (AndroidVersion.hasNougat()) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        text
    }
}