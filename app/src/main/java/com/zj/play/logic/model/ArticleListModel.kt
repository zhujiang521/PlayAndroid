package com.zj.play.logic.model

data class ArticleListModel(
    val curPage: Int,
    val datas: List<ArticleModel>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)