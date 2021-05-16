package com.zj.play.logic.model

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/5/15
 * 描述：PlayAndroid
 *
 */

data class BaseModel<T>(
    val `data`: T,
    val errorCode: Int,
    val errorMsg: String
)