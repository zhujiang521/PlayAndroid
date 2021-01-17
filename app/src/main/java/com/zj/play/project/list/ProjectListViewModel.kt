package com.zj.play.project.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseAndroidViewModel
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.entity.Article
import com.zj.play.project.ProjectRepository
import dagger.hilt.android.scopes.FragmentScoped

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/18
 * 描述：PlayAndroid
 *
 */
@FragmentScoped
class ProjectListViewModel @ViewModelInject constructor(
    private val projectRepository: ProjectRepository
) : BaseAndroidViewModel<List<Article>, Article, QueryArticle>() {

    override fun getData(page: QueryArticle): LiveData<Result<List<Article>>> {
        return projectRepository.getProject(page)
    }

}