package com.zj.play.network

import com.zj.play.model.LoginModel
import com.zj.play.model.BaseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
interface LoginService {

    @POST("user/login")
    fun getLogin(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<LoginModel>

    @POST("user/register")
    fun getRegister(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") repassword: String
    ): Call<LoginModel>

    @GET("user/logout/json")
    fun getLogout(): Call<BaseModel>

}