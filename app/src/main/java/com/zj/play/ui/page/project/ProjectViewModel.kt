package com.zj.play.ui.page.project

import android.app.Application
import com.zj.play.logic.base.repository.BaseArticlePagingRepository
import com.zj.play.logic.base.viewmodel.BaseViewModel

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/17
 * 描述：PlayAndroid
 *
 */
class ProjectViewModel(application: Application) : BaseViewModel(application) {

    override val repositoryArticle: BaseArticlePagingRepository
        get() = ProjectRepository(getApplication())

    override suspend fun getData() {
        (repositoryArticle as ProjectRepository).getTree(mutableTreeLiveData)
    }

}