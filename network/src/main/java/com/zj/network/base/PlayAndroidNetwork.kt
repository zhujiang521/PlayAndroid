package com.zj.network.base

import com.zj.network.service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/4/30
 * 描述：SunnyWeather
 *
 */
object PlayAndroidNetwork {

    private val homePageService = ServiceCreator.create(HomePageService::class.java)

    suspend fun getBanner() = homePageService.getBanner().await()

    suspend fun getTopArticleList() = homePageService.getTopArticle().await()

    suspend fun getArticleList(page: Int) = homePageService.getArticle(page).await()

    suspend fun getHotKey() = homePageService.getHotKey().await()

    suspend fun getQueryArticleList(page: Int, k: String) =
        homePageService.getQueryArticleList(page, k).await()

    private val projectService = ServiceCreator.create(ProjectService::class.java)

    suspend fun getProjectTree() = projectService.getProjectTree().await()

    suspend fun getProject(page: Int, cid: Int) = projectService.getProject(page, cid).await()

    private val officialService = ServiceCreator.create(OfficialService::class.java)

    suspend fun getWxArticleTree() = officialService.getWxArticleTree().await()

    suspend fun getWxArticle(page: Int, cid: Int) = officialService.getWxArticle(page, cid).await()

    private val loginService = ServiceCreator.create(LoginService::class.java)

    suspend fun getLogin(username: String, password: String) =
        loginService.getLogin(username, password).await()

    suspend fun getRegister(username: String, password: String, repassword: String) =
        loginService.getRegister(username, password, repassword).await()

    suspend fun getLogout() = loginService.getLogout().await()

    private val shareService = ServiceCreator.create(ShareService::class.java)

    suspend fun getMyShareList(page: Int) = shareService.getMyShareList(page).await()

    suspend fun getShareList(cid: Int, page: Int) = shareService.getShareList(cid, page).await()

    suspend fun deleteMyArticle(cid: Int) = shareService.deleteMyArticle(cid).await()

    suspend fun shareArticle(title: String, link: String) =
        shareService.shareArticle(title, link).await()

    private val rankService = ServiceCreator.create(RankService::class.java)

    suspend fun getRankList(page: Int) = rankService.getRankList(page).await()

    suspend fun getUserRank(page: Int) = rankService.getUserRank(page).await()

    suspend fun getUserInfo() = rankService.getUserInfo().await()

    private val collectService = ServiceCreator.create(CollectService::class.java)

    suspend fun getCollectList(page: Int) = collectService.getCollectList(page).await()

    suspend fun toCollect(id: Int) = collectService.toCollect(id).await()

    suspend fun cancelCollect(id: Int) = collectService.cancelCollect(id).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}