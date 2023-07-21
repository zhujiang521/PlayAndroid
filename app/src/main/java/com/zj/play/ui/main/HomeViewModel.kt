package com.zj.play.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zj.model.*
import com.zj.play.logic.viewmodel.ArticleListInterface
import com.zj.play.ui.main.nav.CourseTabs
import com.zj.play.ui.page.home.HomeArticlePagingRepository
import com.zj.utils.XLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/17
 * 描述：PlayAndroid
 *
 */
class HomeViewModel(application: Application) : AndroidViewModel(application),
    ArticleListInterface {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _position = MutableLiveData(CourseTabs.HOME_PAGE)
    val position: LiveData<CourseTabs> = _position
    override val repositoryArticle = HomeArticlePagingRepository()

    override val searchResults = MutableSharedFlow<Query>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val articleResult: Flow<PagingData<ArticleModel>> = searchResults.flatMapLatest {
        repositoryArticle.getPagingData(it)
    }.cachedIn(viewModelScope)

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

    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    fun refresh() {
        // This doesn't handle multiple 'refreshing' tasks, don't use this
        viewModelScope.launch {
            // A fake 2 second 'refresh'
            _isRefreshing.emit(true)
            val time = measureTimeMillis {
                getBanner()
                getHomeArticle()
            }
            delay(if (time > 1000L) time else 1000L)
            _isRefreshing.emit(false)
        }
    }

    fun getBanner() {
        if (bannerState.value is PlaySuccess<*>) {
            XLog.e("Do not request existing data")
            return
        }
        bannerJob?.cancel()
        bannerJob = viewModelScope.launch(Dispatchers.IO) {
            repositoryArticle.getBanner(_bannerState, getApplication())
        }
    }

}