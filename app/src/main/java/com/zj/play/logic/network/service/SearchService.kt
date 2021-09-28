package com.zj.play.logic.network.service

import com.zj.play.logic.model.ArticleListModel
import com.zj.play.logic.model.BaseModel
import com.zj.play.logic.model.ClassifyModel
import com.zj.play.logic.model.HotkeyModel
import retrofit2.http.GET
import retrofit2.http.POST
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
interface SearchService {

    @GET("hotkey/json")
    suspend fun getHotkeyModel(): BaseModel<List<HotkeyModel>>


    @POST("article/query/{page}/json")
    suspend fun getSearchArticleList(
        @Path("page") page: Int,
        @Query("k") keyword: String
    ): BaseModel<ArticleListModel>

}