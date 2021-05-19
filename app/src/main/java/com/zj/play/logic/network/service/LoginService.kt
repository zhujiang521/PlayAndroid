package com.zj.play.logic.network.service

import com.zj.play.logic.model.BaseModel
import com.zj.play.logic.model.LoginModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/19
 * 描述：PlayAndroid
 *
 */
interface LoginService {

    @POST("user/login")
    suspend fun getLogin(
        @Query("username") username: String,
        @Query("password") password: String
    ): BaseModel<LoginModel>

    @POST("user/register")
    suspend fun getRegister(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") repassword: String
    ): BaseModel<LoginModel>

    @GET("user/logout/json")
    suspend fun getLogout(): BaseModel<Any>

}