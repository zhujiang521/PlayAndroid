package com.zj.play.model

import com.zj.play.room.entity.HotKey

data class HotKeyModel(
    val `data`: List<HotKey>,
    val errorCode: Int,
    val errorMsg: String
)