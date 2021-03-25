package com.zj.play.compose.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.play.compose.model.PlayState
import com.zj.play.compose.repository.BasePagingRepository
import com.zj.play.compose.repository.HomePagingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */

class HomePageViewModel(application: Application) : BaseArticleViewModel(application) {

    override val repositoryArticle: BasePagingRepository
        get() = HomePagingRepository(application = getApplication())

    private var bannerJob: Job? = null

    private val _bannerState = MutableLiveData<PlayState>()

    val bannerState: LiveData<PlayState>
        get() = _bannerState

    fun getBanner() {
        bannerJob?.cancel()
        bannerJob = viewModelScope.launch(Dispatchers.IO) {
            (repositoryArticle as HomePagingRepository).getBanner(_bannerState)
        }
    }

}

const val REFRESH_START = 1
const val REFRESH_STOP = 2
