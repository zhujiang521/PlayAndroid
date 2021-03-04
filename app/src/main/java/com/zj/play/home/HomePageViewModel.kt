package com.zj.play.home

import android.app.Application
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.zj.model.pojo.QueryHomeArticle
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import com.zj.play.compose.model.PlayState
import com.zj.play.home.almanac.ShareState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */

class HomePageViewModel(application: Application) : AndroidViewModel(application) {

    private val homeRepository:HomeRepository = HomeRepository(application)

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

    fun getBanner() = homeRepository.getBanner()

    fun getArticleList(page: Int, isRefresh: Boolean) {
        Log.e("ZHUJIANG123", "getArticleList: 111")
        //pageLiveData.value = QueryHomeArticle(page, isRefresh)
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getArticleList(_state,QueryHomeArticle(page, isRefresh))
        }
    }

}