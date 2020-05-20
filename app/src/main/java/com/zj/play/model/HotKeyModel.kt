package com.zj.play.model

data class HotKeyModel(
    val `data`: List<HotKey>,
    val errorCode: Int,
    val errorMsg: String
)

data class HotKey(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)