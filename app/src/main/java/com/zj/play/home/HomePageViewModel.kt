package com.zj.play.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zj.model.pojo.QueryHomeArticle
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
@HiltViewModel
class HomePageViewModel @Inject constructor(
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