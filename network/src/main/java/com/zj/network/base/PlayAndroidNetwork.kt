package com.zj.network.base

import com.zj.network.service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/4/30
 * 描述：SunnyWeather
 *
 */
object PlayAndroidNetwork {

    private val homePageService = ServiceCreator.create(HomePageService::class.java)

    suspend fun getBanner() = homePageService.getBanner()

    suspend fun getTopArticleList() = homePageService.getTopArticle()

    suspend fun getArticleList(page: Int) = homePageService.getArticle(page)

    suspend fun getHotKey() = homePageService.getHotKey()

    suspend fun getQueryArticleList(page: Int, k: String) =
        homePageService.getQueryArticleList(page, k)

    private val projectService = ServiceCreator.create(ProjectService::class.java)

    suspend fun getProjectTree() = projectService.getProjectTree()

    suspend fun getProject(page: Int, cid: Int) = projectService.getProject(page, cid)

    private val officialService = ServiceCreator.create(OfficialService::class.java)

    suspend fun getWxArticleTree() = officialService.getWxArticleTree()

    suspend fun getWxArticle(page: Int, cid: Int) = officialService.getWxArticle(page, cid)

    private val loginService = ServiceCreator.create(LoginService::class.java)

    suspend fun getLogin(username: String, password: String) =
        loginService.getLogin(username, password)

    suspend fun getRegister(username: String, password: String, repassword: String) =
        loginService.getRegister(username, password, repassword)

    suspend fun getLogout() = loginService.getLogout()

    private val shareService = ServiceCreator.create(ShareService::class.java)

    suspend fun getMyShareList(page: Int) = shareService.getMyShareList(page)

    suspend fun getShareList(cid: Int, page: Int) = shareService.getShareList(cid, page)

    suspend fun deleteMyArticle(cid: Int) = shareService.deleteMyArticle(cid)

    suspend fun shareArticle(title: String, link: String) =
        shareService.shareArticle(title, link)

    private val rankService = ServiceCreator.create(RankService::class.java)

    suspend fun getRankList(page: Int) = rankService.getRankList(page)

    suspend fun getUserRank(page: Int) = rankService.getUserRank(page)

    suspend fun getUserInfo() = rankService.getUserInfo()

    private val collectService = ServiceCreator.create(CollectService::class.java)

    suspend fun getCollectList(page: Int) = collectService.getCollectList(page)

    suspend fun toCollect(id: Int) = collectService.toCollect(id)

    suspend fun cancelCollect(id: Int) = collectService.cancelCollect(id)

}