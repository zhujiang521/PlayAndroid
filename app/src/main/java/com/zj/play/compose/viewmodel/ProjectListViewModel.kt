package com.zj.play.compose.viewmodel

import android.app.Application
import com.zj.play.compose.repository.BasePagingRepository
import com.zj.play.compose.repository.ProjectPagingRepository

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/18
 * 描述：PlayAndroid
 *
 */
class ProjectListViewModel(application: Application) : BaseArticleViewModel(application) {

    override val repositoryArticle: BasePagingRepository
        get() = ProjectPagingRepository(application = getApplication())

}