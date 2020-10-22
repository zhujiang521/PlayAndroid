package com.zj.play.view.project.list

import android.app.Application
import androidx.lifecycle.LiveData
import com.zj.core.view.BaseAndroidViewModel
import com.zj.play.room.entity.Article
import com.zj.play.view.project.ProjectRepository

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/18
 * 描述：PlayAndroid
 *
 */
class ProjectListViewModel(application: Application) : BaseAndroidViewModel<List<Article>,Article,QueryArticle>(application) {

    override fun getData(page: QueryArticle): LiveData<Result<List<Article>>> {
        return ProjectRepository(getApplication()).getProject(page)
    }

}

data class QueryArticle(var page: Int, var cid: Int, var isRefresh: Boolean)