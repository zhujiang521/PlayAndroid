package com.zj.play.model

data class BannerModel(
    val `data`: List<BannerBean>,
    val errorCode: Int,
    val errorMsg: String
)

data class BannerBean(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)