package com.zj.play.logic.utils

import android.text.Html

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：21/5/16
 * 描述：PlayAndroid
 *
 */

/**
 * 将Html文本转为普通字符串
 */
fun getHtmlText(text: String): String {
    return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
}