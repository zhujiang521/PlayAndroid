package com.zj.model

data class NavigationModel(
    val articles: List<ArticleModel>,
    val cid: Int,
    val name: String
)