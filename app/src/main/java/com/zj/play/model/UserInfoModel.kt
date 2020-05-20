package com.zj.play.model

data class UserInfoModel(
    val `data`: UserInfo,
    val errorCode: Int,
    val errorMsg: String
)

data class UserInfo(
    val coinCount: Int,
    val rank: Int,
    val userId: Int,
    val username: String
)