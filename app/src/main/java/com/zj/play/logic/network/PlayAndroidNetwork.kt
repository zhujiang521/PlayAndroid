package com.zj.play.logic.network

import com.zj.play.logic.network.service.HomePageService
import com.zj.play.logic.network.service.LoginService
import com.zj.play.logic.network.service.OfficialService
import com.zj.play.logic.network.service.ProjectService

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/4/30
 * 描述：SunnyWeather
 *
 */
object PlayAndroidNetwork {

    private val homePageService = ServiceCreator.create(HomePageService::class.java)

    suspend fun getBanner() = homePageService.getBanner()

    suspend fun getArticle(page: Int) = homePageService.getArticle(page)

    private val projectService = ServiceCreator.create(ProjectService::class.java)

    suspend fun getProjectTree() = projectService.getProjectTree()

    suspend fun getProject(page: Int, cid: Int) = projectService.getProject(page, cid)

    private val officialService = ServiceCreator.create(OfficialService::class.java)

    suspend fun getWxArticleTree() = officialService.getWxArticleTree()

    suspend fun getWxArticle(page: Int, cid: Int) = officialService.getWxArticle(page, cid)

    private val loginService = ServiceCreator.create(LoginService::class.java)

    suspend fun getLogin(username: String, password: String) =
        loginService.getLogin(username, password)

    suspend fun getRegister(username: String, password: String, rePassword: String) =
        loginService.getRegister(username, password, rePassword)

    suspend fun getLogout() = loginService.getLogout()
}