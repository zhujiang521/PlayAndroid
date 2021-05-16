package com.zj.play.logic.base.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.play.logic.model.PlayState
import com.zj.play.ui.page.home.REFRESH_STOP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/10/18
 * 描述：PlayAndroid
 *
 */

abstract class BaseViewModel<Key>(application: Application) : BaseArticleViewModel(application) {
    private val _refreshState = MutableLiveData<Int>()

    val refreshState: LiveData<Int>
        get() = _refreshState

    fun onRefreshChanged(refresh: Int) {
        _refreshState.postValue(refresh)
    }

    private val _loadRefreshState = MutableLiveData(REFRESH_STOP)

    val loadRefreshState: LiveData<Int>
        get() = _loadRefreshState

    fun onLoadRefreshStateChanged(refresh: Int) {
        _loadRefreshState.postValue(refresh)
    }

    protected val mutableLiveData = MutableLiveData<PlayState>()

    val dataLiveData: LiveData<PlayState>
        get() = mutableLiveData


    private val pageLiveData = MutableLiveData<Key>()

    private val _page = MutableLiveData<Int>()

    val page: LiveData<Int>
        get() = _page

    fun onPageChanged(refresh: Int) {
        _page.postValue(refresh)
    }

    abstract suspend fun getData(page: Key)

    fun getDataList(page: Key) {
        viewModelScope.launch(Dispatchers.IO) {
            getData(page)
        }
    }

    private val _position = MutableLiveData(0)
    val position: LiveData<Int> = _position

    fun onPositionChanged(position: Int) {
        _position.value = position
    }

}