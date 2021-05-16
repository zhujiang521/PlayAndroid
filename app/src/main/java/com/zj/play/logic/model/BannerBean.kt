package com.zj.play.logic.model

import com.zj.banner.model.BaseBannerBean

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/5/15
 * 描述：PlayAndroid
 *
 */

data class BannerBean(
    val uid: Int = 0,
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String,
    var filePath: String = "",
    override var data: String?
) : BaseBannerBean()