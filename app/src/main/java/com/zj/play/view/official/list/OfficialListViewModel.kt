package com.zj.play.view.official.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zj.play.model.Article
import com.zj.play.network.Repository
import com.zj.play.view.project.list.QueryArticle

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class OfficialListViewModel : ViewModel() {

    val articleList = ArrayList<Article>()

    private val pageLiveData = MutableLiveData<QueryArticle>()

    val articleLiveData = Transformations.switchMap(pageLiveData) { query ->
        Repository.getWxArticle(query.page, query.cid)
    }

    fun getArticleList(page: Int, cid: Int) {
        pageLiveData.value = QueryArticle(page, cid)
    }

}