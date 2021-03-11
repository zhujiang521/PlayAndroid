package com.zj.play.compose.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.zj.model.pojo.QueryHomeArticle
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import com.zj.model.room.entity.ProjectClassify
import com.zj.play.compose.model.PlayState
import com.zj.play.home.HomeRepository
import com.zj.play.compose.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */

class HomePageViewModel(application: Application) : AndroidViewModel(application) {

    private val homeRepository: HomeRepository = HomeRepository(application)

    private val pageLiveData = MutableLiveData<QueryHomeArticle>()

    val bannerList = ArrayList<BannerBean>()

    val bannerList2 = ArrayList<BannerBean>()

    val articleList = ArrayList<Article>()

    private val _state = MutableLiveData<PlayState>()

    val state: LiveData<PlayState>
        get() = _state

    val articleLiveData = Transformations.switchMap(pageLiveData) { query ->
        homeRepository.getArticleList(query)
    }

    private val _bannerState = MutableLiveData<PlayState>()

    val bannerState: LiveData<PlayState>
        get() = _bannerState

    fun getBanner() {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getBanner(_bannerState)
        }
    }

    fun getArticleList(page: Int, isRefresh: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getArticleList(_state, QueryHomeArticle(page, isRefresh))
        }
    }

    private val _refreshState = MutableLiveData<Int>()

    val refreshState: LiveData<Int>
        get() = _refreshState

    fun onRefreshChanged(refresh: Int) {
        _refreshState.postValue(refresh)
    }

}

const val REFRESH_DEFAULT = 0
const val REFRESH_START = 1
const val REFRESH_STOP = 2

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class ProjectViewModel(application: Application) :
    BaseAndroidViewModel<List<ProjectClassify>, Unit, Boolean>(application) {

    private val projectRepository = ProjectRepository(application)

    override suspend fun getData(page: Boolean) {
        projectRepository.getProjectTree(_state, page)
    }

}