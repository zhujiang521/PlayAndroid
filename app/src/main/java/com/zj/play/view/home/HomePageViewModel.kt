package com.zj.play.view.home

import androidx.lifecycle.*
import com.zj.play.model.BannerBean
import com.zj.play.model.Article
import com.zj.play.network.Repository

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

    val articleList = ArrayList<Article>()

    fun getBanner(): LiveData<Result<List<BannerBean>>> {
       return Repository.getBanner()
    }

    val articleLiveData = Transformations.switchMap(pageLiveData) { page ->
        Repository.getArticleList(page)
    }

    fun getArticleList(page: Int) {
        pageLiveData.value = page
    }

}