package com.zj.play.logic.paging

import com.zj.model.ArticleModel
import com.zj.network.PlayAndroidNetwork
import com.zj.utils.XLog

class HomePagingSource : BasePagingSource() {

    override suspend fun getArticleList(page: Int): List<ArticleModel> {
        XLog.i("Obtain the home page list data")
        val apiResponse = PlayAndroidNetwork.getArticle(page)
        return apiResponse.data.datas
    }
}

class OfficialPagingSource(
    private val cid: Int
) : BasePagingSource() {

    override suspend fun getArticleList(page: Int): List<ArticleModel> {
        val apiResponse = PlayAndroidNetwork.getWxArticle(page, cid)
        return apiResponse.data.datas
    }
}

class ProjectPagingSource(private val cid: Int) : BasePagingSource() {

    override suspend fun getArticleList(page: Int): List<ArticleModel> {
        val apiResponse = PlayAndroidNetwork.getProject(page, cid)
        return apiResponse.data.datas
    }
}

class SearchPagingSource(private val keyword: String) : BasePagingSource() {

    override suspend fun getArticleList(page: Int): List<ArticleModel> {
        val apiResponse = PlayAndroidNetwork.getSearchArticleList(page, keyword)
        return apiResponse.data.datas
    }
}

class SystemPagingSource(private val cid: Int) : BasePagingSource() {

    override suspend fun getArticleList(page: Int): List<ArticleModel> {
        XLog.i("getArticleList: page:$page   cid:$cid")
        val apiResponse = PlayAndroidNetwork.getSystemArticle(page, cid)
        return apiResponse.data.datas
    }
}