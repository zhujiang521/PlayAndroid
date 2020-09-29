package com.zj.play.view.official.list

import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.*
import com.zj.play.App
import com.zj.play.room.entity.Article
import com.zj.play.view.home.search.article.ArticleListActivity
import com.zj.play.view.official.OfficialRepository
import com.zj.play.view.project.list.QueryArticle

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class OfficialListViewModel(application: Application) : AndroidViewModel(application) {

    val articleList = ArrayList<Article>()

    private val pageLiveData = MutableLiveData<QueryArticle>()

    val articleLiveData = Transformations.switchMap(pageLiveData) { query ->
        OfficialRepository(getApplication()).getWxArticle(query)
    }

    fun getArticleList(page: Int, cid: Int, isRefresh: Boolean) {
        pageLiveData.value = QueryArticle(page, cid, isRefresh)
    }

}

