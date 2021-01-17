package com.zj.play.home.search.article

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseAndroidViewModel
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
class ArticleListViewModel @ViewModelInject constructor(
    private val searchRepository: SearchRepository
) : BaseAndroidViewModel<ArticleList, Article, QueryKeyArticle>() {

    override fun getData(page: QueryKeyArticle): LiveData<Result<ArticleList>> {
        return searchRepository.getQueryArticleList(page.page, page.k)
    }

}

data class QueryKeyArticle(var page: Int, var k: String)