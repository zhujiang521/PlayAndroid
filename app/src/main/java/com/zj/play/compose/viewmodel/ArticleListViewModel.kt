package com.zj.play.compose.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zj.model.room.entity.Article
import com.zj.play.compose.repository.SearchRepository

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */

class ArticleListViewModel(application: Application) :
    BaseAndroidViewModel<QueryKeyArticle>(application) {

    private val searchRepository = SearchRepository(application)

    private val _articleDataList = MutableLiveData<ArrayList<Article>>()

    override suspend fun getData(page: QueryKeyArticle) {
        searchRepository.getQueryArticleList(mutableLiveData, _articleDataList,page.page, page.k)
    }

}

data class QueryKeyArticle(var page: Int, var k: String)