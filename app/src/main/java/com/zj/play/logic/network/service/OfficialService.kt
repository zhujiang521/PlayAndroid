package com.zj.play.logic.network.service


import com.zj.play.logic.model.ArticleListModel
import com.zj.play.logic.model.BaseModel
import com.zj.play.logic.model.ClassifyModel
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/19
 * 描述：PlayAndroid
 *
 */
interface OfficialService {

    @GET("wxarticle/chapters/json")
    suspend fun getWxArticleTree(): BaseModel<List<ClassifyModel>>

    @GET("wxarticle/list/{cid}/{page}/json")
    suspend fun getWxArticle(@Path("page") page: Int, @Path("cid") cid: Int): BaseModel<ArticleListModel>

}