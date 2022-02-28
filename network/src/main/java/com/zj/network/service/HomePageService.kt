package com.zj.network.service


import com.zj.model.ArticleListModel
import com.zj.model.BannerBean
import com.zj.model.BaseModel
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/16
 * 描述：SunnyWeather
 *
 */
interface HomePageService {

    @GET("banner/json")
    suspend fun getBanner(): BaseModel<List<BannerBean>>

    @GET("article/list/{a}/json")
    suspend fun getArticle(@Path("a") a: Int): BaseModel<ArticleListModel>

}