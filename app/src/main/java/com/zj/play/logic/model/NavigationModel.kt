package com.zj.play.logic.model

data class NavigationModel(
    val articles: List<ArticleModel>,
    val cid: Int,
    val name: String
)