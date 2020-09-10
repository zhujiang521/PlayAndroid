package com.zj.play.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zj.play.model.Article
import com.zj.play.model.BannerBean

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class HomePageViewModel : ViewModel() {

    private val pageLiveData = MutableLiveData<Int>()

    val bannerList = ArrayList<BannerBean>()

    val bannerList2 = ArrayList<BannerBean>()

    val articleList = ArrayList<Article>()

    val articleLiveData = Transformations.switchMap(pageLiveData) { page ->
        HomeRepository.getArticleList(page)
    }

    val bannerLists =  HomeRepository.getBanner()

    fun getArticleList(page: Int) {
        pageLiveData.value = page
    }

}