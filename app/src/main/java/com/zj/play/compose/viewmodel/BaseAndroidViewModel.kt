package com.zj.play.compose.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.zj.play.compose.model.PlayState
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

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
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

}

abstract class BaseAndroidViewModel<Key>(application: Application) :
    BaseViewModel(application) {

    private val pageLiveData = MutableLiveData<Key>()

    private val _page = MutableLiveData<Int>()

    val page: LiveData<Int>
        get() = _page

    fun onPageChanged(refresh: Int) {
        _page.postValue(refresh)
    }

    protected val mutableLiveData = MutableLiveData<PlayState>()

    val dataLiveData: LiveData<PlayState>
        get() = mutableLiveData

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