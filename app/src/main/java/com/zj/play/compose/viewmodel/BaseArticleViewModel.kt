package com.zj.play.compose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.zj.model.room.entity.Article
import com.zj.play.compose.repository.BasePagingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

abstract class BaseArticleViewModel(application: Application) : AndroidViewModel(application) {

    abstract val repositoryArticle: BasePagingRepository

    private val searchResults = MutableSharedFlow<Int>(replay = 1)

    @ExperimentalPagingApi
    @OptIn(ExperimentalCoroutinesApi::class)
    val articleResult: Flow<PagingData<Article>> = searchResults.flatMapLatest {
        repositoryArticle.getPagingData(it)
    }

    private var searchJob: Job? = null

    /**
     * Search a repository based on a query string.
     */
    open fun searchArticle(cid: Int) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchResults.emit(cid)
        }
    }


}