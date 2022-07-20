package com.zj.play.project

import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseAndroidViewModel
import com.zj.model.room.entity.ProjectClassify
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) :
    BaseAndroidViewModel<List<ProjectClassify>, Unit, Boolean>() {

    var position = 0

    override fun getData(page: Boolean): LiveData<Result<List<ProjectClassify>>> {
        return projectRepository.getProjectTree(page)
    }

    init {
        getDataList(false)
    }

}