package com.zj.play.view.official

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class OfficialViewModel(application: Application) : AndroidViewModel(application) {

    var position = 0

    private val refreshLiveData = MutableLiveData<Boolean>()

    val officialTreeLiveData = Transformations.switchMap(refreshLiveData) { isRefresh ->
        OfficialRepository(application).getWxArticleTree(isRefresh)
    }

    fun getArticleList(isRefresh: Boolean) {
        refreshLiveData.value = isRefresh
    }
}