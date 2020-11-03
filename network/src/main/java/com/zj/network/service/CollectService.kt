package com.zj.network.service

import com.zj.model.model.BaseModel
import com.zj.model.model.Collect
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
interface CollectService {

    @GET("lg/collect/list/{page}/json")
    fun getCollectList(@Path("page") page: Int): Call<BaseModel<Collect>>

    @POST("lg/collect/{id}/json")
    fun toCollect(@Path("id") id: Int): Call<BaseModel<Any>>

    @POST("lg/uncollect_originId/{id}/json")
    fun cancelCollect(@Path("id") id: Int): Call<BaseModel<Any>>

}