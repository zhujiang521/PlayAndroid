package com.zj.play.view.project.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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
class ProjectListViewModel(application: Application) : AndroidViewModel(application) {

    val articleList = ArrayList<Article>()

    private val pageLiveData = MutableLiveData<QueryArticle>()

    val articleLiveData = Transformations.switchMap(pageLiveData) { query ->
        ProjectRepository(application).getProject(query)
    }

    fun getArticleList(page: Int, cid: Int, isRefresh: Boolean) {
        pageLiveData.value = QueryArticle(page, cid, isRefresh)
    }

}

data class QueryArticle(var page: Int, var cid: Int, var isRefresh: Boolean)