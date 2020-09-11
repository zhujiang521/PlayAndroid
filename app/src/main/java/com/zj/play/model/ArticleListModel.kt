package com.zj.play.model

import com.zj.play.room.entity.Article

data class ArticleListModel(
    val `data`: ArticleList,
    val errorCode: Int,
    val errorMsg: String
)

data class ArticleList(
    val curPage: Int,
    val datas: List<Article>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class Tag(
    val name: String,
    val url: String
)