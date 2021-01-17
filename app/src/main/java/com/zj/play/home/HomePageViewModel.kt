package com.zj.play.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zj.model.pojo.QueryHomeArticle
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class HomePageViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val pageLiveData = MutableLiveData<QueryHomeArticle>()

    val bannerList = ArrayList<BannerBean>()

    val bannerList2 = ArrayList<BannerBean>()

    val articleList = ArrayList<Article>()

    val articleLiveData = Transformations.switchMap(pageLiveData) { query ->
        homeRepository.getArticleList(query)
    }

    fun getBanner() = homeRepository.getBanner()

    fun getArticleList(page: Int, isRefresh: Boolean) {
        pageLiveData.value = QueryHomeArticle(page, isRefresh)
    }

}