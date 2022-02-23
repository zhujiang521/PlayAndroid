package com.zj.play.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zj.play.logic.base.repository.BaseArticlePagingRepository
import com.zj.play.logic.base.viewmodel.BaseArticleViewModel
import com.zj.play.logic.model.*
import com.zj.play.logic.utils.XLog
import com.zj.play.ui.main.nav.CourseTabs
import com.zj.play.ui.page.home.HomeArticlePagingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/17
 * 描述：PlayAndroid
 *
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _position = MutableLiveData(CourseTabs.HOME_PAGE)
    val position: LiveData<CourseTabs> = _position
    private val repositoryArticle = HomeArticlePagingRepository()

    private val searchResults = MutableSharedFlow<Query>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val articleResult: Flow<PagingData<ArticleModel>> = searchResults.flatMapLatest {
        repositoryArticle.getPagingData(it)
    }.cachedIn(viewModelScope)

    /**
     * Search a repository based on a query string.
     */
    private suspend fun searchArticle(query: Query) {
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

    fun onPositionChanged(position: CourseTabs) {
        _position.value = position
    }

    private var bannerJob: Job? = null

    private val _bannerState = MutableLiveData<PlayState<List<BannerBean>>>()

    val bannerState: LiveData<PlayState<List<BannerBean>>>
        get() = _bannerState

    fun getHomeArticle() {
        viewModelScope.launch {
            searchArticle(Query(k = TAG))
        }
    }

    fun getBanner() {
        if (bannerState.value is PlaySuccess<*>) {
            XLog.e("已有数据，不进行请求")
            return
        }
        bannerJob?.cancel()
        bannerJob = viewModelScope.launch(Dispatchers.IO) {
            repositoryArticle.getBanner(_bannerState)
        }
    }

}