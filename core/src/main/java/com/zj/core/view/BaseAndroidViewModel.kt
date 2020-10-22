package com.zj.core.view

import android.app.Application
import androidx.lifecycle.*

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/10/18
 * 描述：PlayAndroid
 *
 */
abstract class BaseAndroidViewModel<BaseData, Data, Key>(application: Application) :
    AndroidViewModel(application) {

    val dataList = ArrayList<Data>()

    private val pageLiveData = MutableLiveData<Key>()

    val dataLiveData = Transformations.switchMap(pageLiveData) { page ->
        getData(page)
    }

    abstract fun getData(page: Key): LiveData<Result<BaseData>>

    fun getDataList(page: Key) {
        pageLiveData.value = page
    }

}