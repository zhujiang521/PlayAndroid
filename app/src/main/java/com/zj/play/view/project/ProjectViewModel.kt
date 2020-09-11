package com.zj.play.view.project

import android.content.Context
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
class ProjectViewModel(context: Context) : ViewModel() {

    val projectTreeLiveData =
        ProjectRepository(context).getProjectTree()

}

class ProjectViewModelFactory(private val context: Context) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProjectViewModel(context) as T
    }

}