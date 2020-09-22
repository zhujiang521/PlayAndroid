package com.zj.play.network

import com.zj.play.model.ArticleList
import com.zj.play.model.BaseModel
import com.zj.play.room.entity.ProjectClassify
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
interface OfficialService {

    @GET("wxarticle/chapters/json")
    fun getWxArticleTree(): Call<BaseModel<List<ProjectClassify>>>

    @GET("wxarticle/list/{cid}/{page}/json")
    fun getWxArticle(@Path("page") page: Int, @Path("cid") cid: Int): Call<BaseModel<ArticleList>>

}