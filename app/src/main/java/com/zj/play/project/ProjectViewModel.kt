package com.zj.play.project

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zj.core.view.base.BaseAndroidViewModel
import com.zj.model.room.entity.ProjectClassify

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class ProjectViewModel(application: Application) :
    BaseAndroidViewModel<List<ProjectClassify>, Unit, Boolean>(application) {

    private val projectRepository = ProjectRepository(application)

    private val _position = MutableLiveData(0)
    val position: LiveData<Int> = _position

    fun onPositionChanged(position: Int) {
        _position.value = position
    }

    override fun getData(page: Boolean): LiveData<List<ProjectClassify>?> {
        return projectRepository.getProjectTree(page)
    }

}