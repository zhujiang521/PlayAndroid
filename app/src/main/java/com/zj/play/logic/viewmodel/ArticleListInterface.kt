package com.zj.play.logic.viewmodel

import androidx.paging.PagingData
import com.zj.model.ArticleModel
import com.zj.model.Query
import com.zj.play.logic.repository.BaseArticlePagingRepository
import com.zj.utils.XLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface ArticleListInterface {

    val repositoryArticle: BaseArticlePagingRepository

    val searchResults: MutableSharedFlow<Query>
        get() = MutableSharedFlow(replay = 1)

    val articleResult: Flow<PagingData<ArticleModel>>

    /**
     * Search a repository based on a query string.
     */
    suspend fun searchArticle(query: Query) {
        XLog.e("searchArticle: $query")
        try {
            XLog.e("searchArticle: one")
            val queryList = searchResults.replayCache
            if (queryList.isNotEmpty()) {
                val q = queryList[0]
                XLog.e("searchArticle: two")
                XLog.e("searchArticle q: $q")
                if (query.k != "" && query.k == q.k || query.cid != -1 && query.cid == q.cid) {
                    return
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            XLog.e("Exception: ${e.message}")
        }
        XLog.e("searchArticle: three")
        searchResults.emit(query)
    }

}