package com.zj.play.logic.base.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.play.logic.model.ClassifyModel
import com.zj.play.logic.model.PlayState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/05/18
 * 描述：PlayAndroid
 *
 */

abstract class BaseViewModel(application: Application) : BaseArticleViewModel(application) {

    protected val mutableTreeLiveData = MutableLiveData<PlayState<List<ClassifyModel>>>()

    val treeLiveData: LiveData<PlayState<List<ClassifyModel>>>
        get() = mutableTreeLiveData

    abstract suspend fun getData()

    fun getDataList() {
        viewModelScope.launch(Dispatchers.IO) {
            getData()
        }
    }

    private val _position = MutableLiveData(0)
    val position: LiveData<Int> = _position

    fun onPositionChanged(position: Int) {
        _position.value = position
    }

}