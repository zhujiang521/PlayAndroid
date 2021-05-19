package com.zj.play.ui.page.project

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zj.play.logic.base.paging.ProjectPagingSource
import com.zj.play.logic.base.repository.BaseArticleRepository
import com.zj.play.logic.model.BaseModel
import com.zj.play.logic.model.ClassifyModel
import com.zj.play.logic.model.Query
import com.zj.play.logic.network.PlayAndroidNetwork

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/9/10
 * 描述：PlayAndroid
 *
 */

class ProjectRepository constructor(val application: Application) :
    BaseArticleRepository(application = application) {

    override suspend fun getArticleTree(): BaseModel<List<ClassifyModel>> {
        return PlayAndroidNetwork.getProjectTree()
    }

    @ExperimentalPagingApi
    override fun getPagingData(query: Query) = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        ProjectPagingSource(query.cid)
    }.flow

}