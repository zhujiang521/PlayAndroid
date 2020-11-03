package com.zj.model.model

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/22
 * 描述：PlayAndroid
 *
 */

data class BaseModel<T>(
    val `data`: T,
    val errorCode: Int,
    val errorMsg: String
)