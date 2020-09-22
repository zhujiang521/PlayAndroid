package com.zj.play.model

/**
 * 版权：联想 版权所有
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