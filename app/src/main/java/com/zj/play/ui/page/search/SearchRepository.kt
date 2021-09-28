package com.zj.play.ui.page.search

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zj.play.logic.base.paging.HomePagingSource
import com.zj.play.logic.base.paging.ProjectPagingSource
import com.zj.play.logic.base.paging.SearchPagingSource
import com.zj.play.logic.base.repository.BaseArticlePagingRepository
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

class SearchRepository constructor(val application: Application) : BaseArticlePagingRepository() {

    @ExperimentalPagingApi
    override fun getPagingData(query: Query) = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        SearchPagingSource(query.k)
    }.flow

}