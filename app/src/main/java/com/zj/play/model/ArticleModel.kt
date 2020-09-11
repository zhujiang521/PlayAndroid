package com.zj.play.model

import com.zj.play.room.entity.Article

data class ArticleModel(
    val `data`: List<Article>,
    val errorCode: Int,
    val errorMsg: String
)
