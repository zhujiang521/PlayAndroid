package com.zj.play.model

data class RankListModel(
    val `data`: RankList,
    val errorCode: Int,
    val errorMsg: String
)

data class RankList(
    val curPage: Int,
    val datas: List<Ranks>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class Ranks(
    val coinCount: Int,
    val date: Long,
    val desc: String,
    val id: Int,
    val reason: String,
    val type: Int,
    val userId: Int,
    val rank: String,
    val userName: String
)