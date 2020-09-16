package com.zj.play.model

import com.zj.play.room.entity.BannerBean

data class BannerModel(
    val `data`: List<BannerBean>,
    val errorCode: Int,
    val errorMsg: String
)
