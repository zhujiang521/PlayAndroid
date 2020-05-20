package com.zj.play.model

data class BaseModel(
    val `data`: Any,
    val errorCode: Int,
    val errorMsg: String
)