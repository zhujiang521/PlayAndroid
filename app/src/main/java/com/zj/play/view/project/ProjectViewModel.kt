package com.zj.play.view.project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    val projectTreeLiveData =
        ProjectRepository(application).getProjectTree()

}