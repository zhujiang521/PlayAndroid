package com.zj.play.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zj.model.pojo.QueryHomeArticle
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import com.zj.network.repository.HomeRepository

/**
 * 版权：Zhujiang 个人版权
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