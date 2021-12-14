package com.zj.play.compose.repository

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.OFFICIAL
import com.zj.model.room.entity.PROJECT
import com.zj.model.room.entity.SEARCH
import com.zj.play.compose.mediator.OfficialRemoteMediator
import com.zj.play.compose.mediator.ProjectRemoteMediator
import com.zj.play.compose.mediator.SearchRemoteMediator
import com.zj.play.compose.model.Query
import kotlinx.coroutines.flow.Flow

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/3/24
 * 描述：PlayAndroid
 *
 */

abstract class BasePagingRepository {
    companion object {
        const val PAGE_SIZE = 15
    }

    abstract fun getPagingData(query: Query): Flow<PagingData<Article>>
}

class ProjectPagingRepository(private val application: Application) : BasePagingRepository() {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingData(query: Query): Flow<PagingData<Article>> {
        val database = PlayDatabase.getDatabase(application)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ProjectRemoteMediator(query.cid, database),
            pagingSourceFactory = {
                database.browseHistoryDao().articleByCid(PROJECT, query.cid)
            }
        ).flow
    }
}

class OfficialPagingRepository(private val application: Application) : BasePagingRepository() {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingData(query: Query): Flow<PagingData<Article>> {
        val database = PlayDatabase.getDatabase(application)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = OfficialRemoteMediator(query.cid, database),
            pagingSourceFactory = {
                database.browseHistoryDao().articleByCid(OFFICIAL, query.cid)
            }
        ).flow
    }
}

class SearchPagingRepository(private val application: Application) : BasePagingRepository() {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingData(query: Query): Flow<PagingData<Article>> {
        val database = PlayDatabase.getDatabase(application)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = SearchRemoteMediator(query.k, database),
            pagingSourceFactory = {
                database.browseHistoryDao().articleByType(SEARCH)
            }
        ).flow
    }
}