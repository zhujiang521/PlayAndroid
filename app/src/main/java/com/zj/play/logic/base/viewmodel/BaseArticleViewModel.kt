package com.zj.play.logic.base.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zj.play.logic.base.repository.BaseArticlePagingRepository
import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.model.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

abstract class BaseArticleViewModel(application: Application) : AndroidViewModel(application) {

    abstract val repositoryArticle: BaseArticlePagingRepository

    private val searchResults = MutableSharedFlow<Query>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val articleResult: Flow<PagingData<ArticleModel>> = searchResults.flatMapLatest {
        repositoryArticle.getPagingData(it)
    }.cachedIn(viewModelScope)

    private var searchJob: Job? = null

    /**
     * Search a repository based on a query string.
     */
    open fun searchArticle(query: Query) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchResults.emit(query)
        }
    }

}