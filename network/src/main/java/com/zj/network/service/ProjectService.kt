package com.zj.network.service

import com.zj.model.model.ArticleList
import com.zj.model.model.BaseModel
import com.zj.model.room.entity.ProjectClassify
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/18
 * 描述：PlayAndroid
 *
 */
interface ProjectService {

    @GET("project/tree/json")
    suspend fun getProjectTree(): BaseModel<List<ProjectClassify>>

    @GET("project/list/{page}/json")
    suspend fun getProject(@Path("page") page: Int, @Query("cid") cid: Int): BaseModel<ArticleList>

}