package com.zj.core.almanac

import java.util.*

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：11/25/20
 * 描述：PlayAndroid
 *
 */

fun isZhLanguage(): Boolean {
    return Locale.getDefault().language == "zh"
}