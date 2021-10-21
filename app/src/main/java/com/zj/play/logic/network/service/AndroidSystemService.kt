package com.zj.play.logic.network.service

import com.zj.play.logic.base.repository.BaseArticlePagingRepository.Companion.PAGE_SIZE
import com.zj.play.logic.model.AndroidSystemModel
import com.zj.play.logic.model.ArticleListModel
import com.zj.play.logic.model.BaseModel
import com.zj.play.logic.model.NavigationModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/19
 * 描述：PlayAndroid
 *
 */
interface AndroidSystemService {

    /**
     * 体系数据
     */
    @GET("tree/json")
    suspend fun getAndroidSystem(): BaseModel<List<AndroidSystemModel>>

    /**
     * 知识体系下的文章
     */
    @GET("article/list/{page}/json")
    suspend fun getSystemArticle(
        @Path("page") page: Int,
        @Query("cid") cid: Int,
        @Query("page_size") page_size: Int = PAGE_SIZE
    ): BaseModel<ArticleListModel>

    /**
     * 按照作者昵称搜索文章
     */
    @GET("article/list/{page}/json")
    suspend fun getNameToSystemArticle(
        @Path("page") page: Int,
        @Query("author") author: String,
        @Query("page_size") page_size: Int = PAGE_SIZE
    ): BaseModel<ArticleListModel>

    /**
     * 获取导航数据
     */
    @GET("navi/json")
    suspend fun getNavigationList(): BaseModel<NavigationModel>

}