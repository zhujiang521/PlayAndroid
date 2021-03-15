package com.zj.play.compose.repository

import android.accounts.NetworkErrorException
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.zj.core.util.DataStoreUtils
import com.zj.core.util.showToast
import com.zj.model.model.ArticleList
import com.zj.model.model.BaseModel
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.PROJECT
import com.zj.model.room.entity.ProjectClassify
import com.zj.network.base.PlayAndroidNetwork
import com.zj.play.R
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlayState
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.home.DOWN_PROJECT_ARTICLE_TIME
import com.zj.play.home.FOUR_HOUR
import kotlinx.coroutines.flow.first

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */

class ProjectRepository constructor(val application: Application) :
    ArticleRepository(application = application) {

    override suspend fun getArticleTree(): BaseModel<List<ProjectClassify>> {
        return PlayAndroidNetwork.getProjectTree()
    }

    override suspend fun getFlag(): String {
        return DOWN_PROJECT_ARTICLE_TIME
    }

    override suspend fun getLocalType(): Int {
        return PROJECT
    }

    override suspend fun getArticleList(page: Int, cid: Int): BaseModel<ArticleList> {
        return PlayAndroidNetwork.getProject(page, cid)
    }

    /**
     * 获取项目标题列表
     */
    suspend fun getProjectTree(state: MutableLiveData<PlayState>, isRefresh: Boolean) {
        super.getTree(state, isRefresh)
    }

    /**
     * 获取项目具体文章列表
     * @param query 查询类
     */
    suspend fun getProject(
        state: MutableLiveData<PlayState>,
        value: MutableLiveData<ArrayList<Article>>,
        query: QueryArticle
    ) {
        super.getArticleList(state, value, query)
    }

}