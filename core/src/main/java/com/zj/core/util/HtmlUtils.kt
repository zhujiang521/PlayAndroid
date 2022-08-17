package com.zj.core.util

import android.text.Html
import java.util.regex.Matcher
import java.util.regex.Pattern

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

/**
 * 判断字符串是否为URL
 *
 * @return true:是URL；false:不是URL
 */
fun String.isHttpUrl(): Boolean {
    var isUrl: Boolean
    //设置正则表达式
    val regex = ("(((https|http)?://)?([a-z0-9]+[.])|(www.))"
            + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)")
    //对比
    val pat: Pattern = Pattern.compile(regex.trim { it <= ' ' })
    val mat: Matcher = pat.matcher(trim { it <= ' ' })
    //判断是否匹配
    isUrl = mat.matches()
    if (isUrl) {
        isUrl = true
    }
    return isUrl
}