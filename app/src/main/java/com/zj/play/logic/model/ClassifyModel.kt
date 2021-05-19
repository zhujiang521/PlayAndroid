package com.zj.play.logic.model

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/9/9
 * 描述：PlayAndroid
 *
 */
data class ClassifyModel(
    val uid: Int,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)