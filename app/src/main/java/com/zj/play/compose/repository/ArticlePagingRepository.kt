package com.zj.play.compose.repository

import android.app.Application
import androidx.paging.*
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.Article
import com.zj.network.base.ServiceCreator
import com.zj.network.service.ArticleService
import com.zj.play.compose.mediator.ArticleRemoteMediator
import kotlinx.coroutines.flow.Flow

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/3/24
 * 描述：PlayAndroid
 *
 */
class ArticlePagingRepository(private val application: Application) {

    companion object {
        private const val PAGE_SIZE = 25
    }

    private val articleService = ServiceCreator.create(ArticleService::class.java)

    fun getPagingData(cid: Int): Flow<PagingData<Article>> {
//        return Pager(
//            config = PagingConfig(PAGE_SIZE),
//            pagingSourceFactory = { ArticlePagingSource(articleService,cid) }
//        ).flow

        val database = PlayDatabase.getDatabase(application)
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ArticleRemoteMediator(
                cid,
                articleService,
                database
            ),
            pagingSourceFactory = {
                database.browseHistoryDao().articleByCid(cid)
            }
        ).flow

    }

}