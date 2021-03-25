package com.zj.play.compose.repository

import android.app.Application
import com.zj.model.model.BaseModel
import com.zj.model.room.entity.ProjectClassify
import com.zj.network.base.PlayAndroidNetwork

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */

class ProjectRepository constructor(val application: Application) :
    ArticleRepository(application = application) {

    override suspend fun getArticleTree(): BaseModel<List<ProjectClassify>> {
        return PlayAndroidNetwork.getProjectTree()
    }

    override suspend fun getFlag(): String {
        return DOWN_PROJECT_ARTICLE_TIME
    }

}