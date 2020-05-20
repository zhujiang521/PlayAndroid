package com.zj.play.model

data class ProjectTreeModel(
    val `data`: List<ProjectTree>,
    val errorCode: Int,
    val errorMsg: String
)

data class ProjectTree(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)