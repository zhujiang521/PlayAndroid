package com.zj.play.ui.page.search

import android.app.Application
import com.zj.play.logic.base.repository.BaseArticlePagingRepository
import com.zj.play.logic.base.viewmodel.BaseArticleViewModel
import com.zj.play.logic.base.viewmodel.BaseViewModel
import com.zj.play.logic.model.Query

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/17
 * 描述：PlayAndroid
 *
 */
class SearchViewModel(application: Application) : BaseArticleViewModel(application) {

    override val repositoryArticle: BaseArticlePagingRepository
        get() = SearchRepository(getApplication())

    fun getSearchArticle(keyword: String) {
        searchArticle(Query(k = keyword))
    }

}