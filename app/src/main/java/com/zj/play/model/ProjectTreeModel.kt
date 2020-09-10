package com.zj.play.model

import com.zj.play.room.entity.ProjectClassify

data class ProjectTreeModel(
    val `data`: List<ProjectClassify>,
    val errorCode: Int,
    val errorMsg: String
)
