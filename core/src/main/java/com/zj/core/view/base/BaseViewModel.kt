package com.zj.core.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/10/18
 * 描述：PlayAndroid
 *
 */
abstract class BaseViewModel<BaseData, Data, Key> : ViewModel() {

    val dataList = ArrayList<Data>()

    private val pageLiveData = MutableLiveData<Key>()

    val dataLiveData = pageLiveData.switchMap { page ->
        getData(page)
    }

    abstract fun getData(page: Key): LiveData<Result<BaseData>>

    fun getDataList(page: Key) {
        pageLiveData.value = page!!
    }

}