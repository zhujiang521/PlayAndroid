package com.zj.play.logic.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.model.ClassifyModel
import com.zj.model.PlayState
import com.zj.model.PlaySuccess
import com.zj.utils.XLog
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

abstract class BaseAndroidViewModel(application: Application) : BaseArticleViewModel(application) {

    protected val mutableTreeLiveData = MutableLiveData<PlayState<List<ClassifyModel>>>()

    val treeLiveData: LiveData<PlayState<List<ClassifyModel>>>
        get() = mutableTreeLiveData

    abstract suspend fun getData()

    fun getDataList() {
        if (mutableTreeLiveData.value is PlaySuccess<*>){
            XLog.e("Do not request existing data")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            getData()
        }
    }

}