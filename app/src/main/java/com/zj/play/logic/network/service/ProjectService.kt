package com.zj.play.logic.network.service

import com.zj.play.logic.model.ArticleListModel
import com.zj.play.logic.model.BaseModel
import com.zj.play.logic.model.ClassifyModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/18
 * 描述：PlayAndroid
 *
 */
interface ProjectService {

    @GET("project/tree/json")
    suspend fun getProjectTree(): BaseModel<List<ClassifyModel>>

    @GET("project/list/{page}/json")
    suspend fun getProject(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseModel<ArticleListModel>

}