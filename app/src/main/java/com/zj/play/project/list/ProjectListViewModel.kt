package com.zj.play.project.list

import android.app.Application
import androidx.lifecycle.LiveData
import com.zj.play.compose.common.BaseAndroidViewModel
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.entity.Article
import com.zj.play.project.ProjectRepository

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/18
 * 描述：PlayAndroid
 *
 */
class ProjectListViewModel(
    application: Application
) : BaseAndroidViewModel<List<Article>, Article, QueryArticle>(application) {

    private val projectRepository = ProjectRepository(application)

    override suspend fun getData(page: QueryArticle) {
        projectRepository.getProject(_state,page)
    }

}