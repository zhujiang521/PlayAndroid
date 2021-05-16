package com.zj.play.logic.base.repository

import androidx.paging.PagingData
import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.model.Query
import kotlinx.coroutines.flow.Flow

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/3/24
 * 描述：PlayAndroid
 *
 */

abstract class BaseArticlePagingRepository {
    companion object {
        const val PAGE_SIZE = 15
    }

    abstract fun getPagingData(query: Query): Flow<PagingData<ArticleModel>>
}