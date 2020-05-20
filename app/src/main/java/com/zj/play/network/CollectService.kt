package com.zj.play.network

import com.zj.play.model.ArticleListModel
import com.zj.play.model.BannerModel
import com.zj.play.model.BaseModel
import com.zj.play.model.CollectModel
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
interface CollectService {

    @GET("lg/collect/list/{page}/json")
    fun getCollectList(@Path("page") page: Int): Call<CollectModel>

    @POST("lg/collect/{id}/json")
    fun toCollect(@Path("id") id: Int): Call<BaseModel>

    @POST("lg/uncollect_originId/{id}/json")
    fun cancelCollect(@Path("id") id: Int): Call<BaseModel>

}