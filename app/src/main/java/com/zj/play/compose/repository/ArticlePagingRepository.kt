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

    abstract fun getPagingData(cid: Int = 0): Flow<PagingData<Article>>
}

class ProjectPagingRepository(private val application: Application) : BasePagingRepository() {

    @ExperimentalPagingApi
    override fun getPagingData(cid: Int): Flow<PagingData<Article>> {
        val database = PlayDatabase.getDatabase(application)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ProjectRemoteMediator(cid, database),
            pagingSourceFactory = {
                database.browseHistoryDao().articleByCid(PROJECT, cid)
            }
        ).flow
    }
}

class OfficialPagingRepository(private val application: Application) : BasePagingRepository() {

    @ExperimentalPagingApi
    override fun getPagingData(cid: Int): Flow<PagingData<Article>> {
        val database = PlayDatabase.getDatabase(application)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = OfficialRemoteMediator(cid, database),
            pagingSourceFactory = {
                database.browseHistoryDao().articleByCid(OFFICIAL, cid)
            }
        ).flow
    }
}

class SearchPagingRepository(private val application: Application) : BasePagingRepository() {

    @ExperimentalPagingApi
    override fun getPagingData(cid: Int): Flow<PagingData<Article>> {
        val database = PlayDatabase.getDatabase(application)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = SearchRemoteMediator("", database),
            pagingSourceFactory = {
                database.browseHistoryDao().articleByType(SEARCH)
            }
        ).flow
    }
}