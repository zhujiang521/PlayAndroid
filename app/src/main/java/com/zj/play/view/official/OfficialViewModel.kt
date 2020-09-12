package com.zj.play.view.official

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class OfficialViewModel(application: Application) : AndroidViewModel(application) {

    val officialTreeLiveData =
        OfficialRepository(application).getProjectTree()
}