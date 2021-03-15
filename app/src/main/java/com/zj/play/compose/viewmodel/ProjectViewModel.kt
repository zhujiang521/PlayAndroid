package com.zj.play.compose.viewmodel

import android.app.Application
import com.zj.model.room.entity.ProjectClassify
import com.zj.play.compose.repository.ProjectRepository

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

    override suspend fun getData(page: Boolean) {
        projectRepository.getProjectTree(_state, page)
    }

}