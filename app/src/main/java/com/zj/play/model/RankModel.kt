package com.zj.play.model

data class RankModel(
    val `data`: Data,
    val errorCode: Int,
    val errorMsg: String
)

data class Data(
    val curPage: Int,
    val datas: List<DataX>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class DataX(
    val coinCount: Int,
    val level: Int,
    val rank: String,
    val userId: Int,
    val username: String
)
