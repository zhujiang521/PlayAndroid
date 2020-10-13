package com.zj.play.view.home.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zj.play.room.entity.HotKey

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val refreshLiveData = MutableLiveData<Boolean>()

    val hotKey = ArrayList<HotKey>()

    val hotKeyLiveData = Transformations.switchMap(refreshLiveData) {
        SearchRepository(application).getHotKey()
    }

    fun getHotKey(isRefresh: Boolean) {
        refreshLiveData.value = isRefresh
    }

}