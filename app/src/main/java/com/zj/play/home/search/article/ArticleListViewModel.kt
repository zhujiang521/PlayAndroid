package com.zj.play.home.search.article

import android.app.Application
import com.zj.play.compose.viewmodel.BaseAndroidViewModel
import com.zj.model.model.ArticleList
import com.zj.model.room.entity.Article
import com.zj.play.home.search.SearchRepository

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class ArticleListViewModel(application: Application) :
    BaseAndroidViewModel<ArticleList, Article, QueryKeyArticle>(application) {

    private val searchRepository = SearchRepository(application)

    override suspend fun getData(page: QueryKeyArticle){
        searchRepository.getQueryArticleList(page.page, page.k)
    }

}

data class QueryKeyArticle(var page: Int, var k: String)