package com.zj.play.compose.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.entity.Article
import com.zj.play.compose.repository.ProjectRepository

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
) : BaseAndroidViewModel<QueryArticle>(application) {

    private val _articleDataList = MutableLiveData<ArrayList<Article>>()

    private val projectRepository = ProjectRepository(application)

    override suspend fun getData(page: QueryArticle) {
        projectRepository.getProject(mutableLiveData,_articleDataList,page)
    }

}