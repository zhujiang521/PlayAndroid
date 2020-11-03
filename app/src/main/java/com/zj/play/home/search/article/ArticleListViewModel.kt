package com.zj.play.home.search.article

import android.app.Application
import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseAndroidViewModel
import com.zj.model.model.ArticleList
import com.zj.model.room.entity.Article
import com.zj.network.repository.SearchRepository

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class ArticleListViewModel(application: Application) : BaseAndroidViewModel<ArticleList, Article, QueryKeyArticle>(application) {

    override fun getData(page: QueryKeyArticle): LiveData<Result<ArticleList>> {
        return SearchRepository(getApplication()).getQueryArticleList(page.page, page.k)
    }

}

data class QueryKeyArticle(var page: Int, var k: String)