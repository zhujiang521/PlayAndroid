package com.zj.play.network

import com.zj.play.model.ArticleListModel
import com.zj.play.model.ArticleModel
import com.zj.play.model.BannerModel
import com.zj.play.model.HotKeyModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/4/30
 * 描述：SunnyWeather
 *
 */
interface HomePageService {

    @GET("banner/json")
    fun getBanner(): Call<BannerModel>

    @GET("article/top/json")
    fun getTopArticle(): Call<ArticleModel>

    @GET("article/list/{a}/json")
    fun getArticle(@Path("a") a: Int): Call<ArticleListModel>

    @GET("hotkey/json")
    fun getHotKey(): Call<HotKeyModel>

    @POST("article/query/{page}/json")
    fun getQueryArticleList(@Path("page") page: Int, @Query("k") k: String): Call<ArticleListModel>

}