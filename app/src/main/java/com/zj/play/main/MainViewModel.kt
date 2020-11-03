package com.zj.play.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class MainViewModel : ViewModel() {

    private val pageLiveData = MutableLiveData<Int>()

    fun setPage(page: Int) {
        pageLiveData.value = page
    }

    fun getPage():Int? {
       return pageLiveData.value
    }

}