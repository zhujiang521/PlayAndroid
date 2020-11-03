package com.zj.play.project.list

import android.app.Application
import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseAndroidViewModel
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.entity.Article
import com.zj.network.repository.ProjectRepository

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/18
 * 描述：PlayAndroid
 *
 */
class ProjectListViewModel(application: Application) : BaseAndroidViewModel<List<Article>, Article, QueryArticle>(application) {

    override fun getData(page: QueryArticle): LiveData<Result<List<Article>>> {
        return ProjectRepository(getApplication()).getProject(page)
    }

}