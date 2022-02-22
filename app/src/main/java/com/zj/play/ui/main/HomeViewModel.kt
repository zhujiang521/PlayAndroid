package com.zj.play.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.play.logic.base.repository.BaseArticlePagingRepository
import com.zj.play.logic.base.viewmodel.BaseArticleViewModel
import com.zj.play.logic.model.BannerBean
import com.zj.play.logic.model.PlayState
import com.zj.play.logic.model.Query
import com.zj.play.ui.main.nav.CourseTabs
import com.zj.play.ui.page.home.HomeArticlePagingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/17
 * 描述：PlayAndroid
 *
 */
class HomeViewModel(application: Application) : BaseArticleViewModel(application) {

    private val _position = MutableLiveData(CourseTabs.HOME_PAGE)
    val position: LiveData<CourseTabs> = _position

    fun onPositionChanged(position: CourseTabs) {
        _position.value = position
    }

    override val repositoryArticle: BaseArticlePagingRepository
        get() = HomeArticlePagingRepository()

    private var bannerJob: Job? = null

    private val _bannerState = MutableLiveData<PlayState<List<BannerBean>>>()

    val bannerState: LiveData<PlayState<List<BannerBean>>>
        get() = _bannerState

    fun getHomeArticle() {
        searchArticle(Query())
    }

    fun getBanner() {
        bannerJob?.cancel()
        bannerJob = viewModelScope.launch(Dispatchers.IO) {
            (repositoryArticle as HomeArticlePagingRepository).getBanner(_bannerState)
        }
    }

}