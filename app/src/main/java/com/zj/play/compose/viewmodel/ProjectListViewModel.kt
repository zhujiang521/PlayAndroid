package com.zj.play.compose.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import com.zj.play.compose.model.QueryArticle
import com.zj.model.room.entity.Article
import com.zj.play.compose.repository.ArticlePagingRepository
import com.zj.play.compose.repository.ProjectRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/18
 * 描述：PlayAndroid
 *
 */
class ProjectListViewModel(
    application: Application
) : BaseAndroidViewModel<QueryArticle>(application) {

    private val repository: ArticlePagingRepository = ArticlePagingRepository(application)

    private val _articleDataList = MutableLiveData<ArrayList<Article>>()

    private val projectRepository = ProjectRepository(application)

    private val searchResults = MutableSharedFlow<Int>(replay = 1)

    override suspend fun getData(page: QueryArticle) {
        projectRepository.getProject(mutableLiveData, _articleDataList, page, page.isLoad)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val repoResult: Flow<PagingData<Article>> = searchResults.flatMapLatest {
        repository.getPagingData(it)
    }

    private var searchJob: Job? = null

    /**
     * Search a repository based on a query string.
     */
    fun searchProject(cid: Int) {
        Log.e("ZHUJIANG123", "searchProject: cid:$cid" )
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchResults.emit(cid)
        }
        Log.e("ZHUJIANG123", "searchProject222" )
    }


}