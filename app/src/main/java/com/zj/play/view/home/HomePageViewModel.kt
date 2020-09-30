package com.zj.play.view.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zj.play.room.entity.Article
import com.zj.play.room.entity.BannerBean

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class HomePageViewModel(application: Application) : AndroidViewModel(application) {

    private val pageLiveData = MutableLiveData<QueryHomeArticle>()

    private val refreshLiveData = MutableLiveData<Boolean>()

    val bannerList = ArrayList<BannerBean>()

    val bannerList2 = ArrayList<BannerBean>()

    val articleList = ArrayList<Article>()

    val articleLiveData = Transformations.switchMap(pageLiveData) { query ->
        HomeRepository.getArticleList(application, query)
    }

    val bannerLiveData = Transformations.switchMap(refreshLiveData) { isRefresh ->
        HomeRepository.getBanner(application,isRefresh)
    }

    fun getBanner(isRefresh: Boolean) {
        refreshLiveData.value = isRefresh
    }

    fun getArticleList(page: Int, isRefresh: Boolean) {
        pageLiveData.value = QueryHomeArticle(page, isRefresh)
    }

}

data class QueryHomeArticle(var page: Int, var isRefresh: Boolean)