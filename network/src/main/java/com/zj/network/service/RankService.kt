package com.zj.network.service

import com.zj.model.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
interface RankService {

    @GET("coin/rank/{page}/json")
    fun getRankList(@Path("page") page: Int): Call<BaseModel<RankData>>

    @GET("lg/coin/userinfo/json")
    fun getUserInfo(): Call<BaseModel<UserInfo>>

    @GET("lg/coin/list/{page}/json")
    fun getUserRank(@Path("page") page: Int): Call<BaseModel<RankList>>

}