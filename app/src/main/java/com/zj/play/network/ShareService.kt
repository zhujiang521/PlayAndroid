package com.zj.play.network

import com.zj.play.model.*
import com.zj.play.room.entity.ProjectClassify
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
interface ShareService {

    @GET("user/{cid}/share_articles/{page}/json")
    fun getShareList(@Path("cid") cid: Int, @Path("page") page: Int): Call<BaseModel<ShareModel>>

    @GET("user/lg/private_articles/{page}/json")
    fun getMyShareList(@Path("page") page: Int): Call<BaseModel<ShareModel>>

    @POST("lg/user_article/delete/{cid}/json")
    fun deleteMyArticle(@Path("cid") cid: Int): Call<BaseModel<Any>>

    @POST("lg/user_article/add/json")
    fun shareArticle(
        @Query("title") title: String,
        @Query("link") link: String
    ): Call<BaseModel<Any>>

}