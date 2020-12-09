package com.zj.network.service

import com.zj.model.model.BaseModel
import com.zj.model.model.ShareModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
interface ShareService {

    @GET("user/{cid}/share_articles/{page}/json")
    suspend fun getShareList(@Path("cid") cid: Int, @Path("page") page: Int): BaseModel<ShareModel>

    @GET("user/lg/private_articles/{page}/json")
    suspend fun getMyShareList(@Path("page") page: Int): BaseModel<ShareModel>

    @POST("lg/user_article/delete/{cid}/json")
    suspend fun deleteMyArticle(@Path("cid") cid: Int): BaseModel<Any>

    @POST("lg/user_article/add/json")
    suspend fun shareArticle(
        @Query("title") title: String,
        @Query("link") link: String
    ): BaseModel<Any>

}