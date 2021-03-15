package com.zj.play.compose.repository

import android.accounts.NetworkErrorException
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.zj.core.util.DataStoreUtils
import com.zj.core.util.showToast
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.PROJECT
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

class ProjectRepository constructor(val application: Application) {

    private val projectClassifyDao = PlayDatabase.getDatabase(application).projectClassifyDao()
    private val articleListDao = PlayDatabase.getDatabase(application).browseHistoryDao()

    /**
     * 获取项目标题列表
     */
    suspend fun getProjectTree(state: MutableLiveData<PlayState>, isRefresh: Boolean) {
        state.postValue(PlayLoading)
        if (!NetworkUtils.isConnected()) {
            showToast(R.string.no_network)
            state.postValue(PlayError(NetworkErrorException("网络未🔗")))
            return
        }
        val projectClassifyLists = projectClassifyDao.getAllProject()
        if (projectClassifyLists.isNotEmpty() && !isRefresh) {
            state.postValue(PlaySuccess(projectClassifyLists))
        } else {
            val projectTree = PlayAndroidNetwork.getProjectTree()
            if (projectTree.errorCode == 0) {
                val projectList = projectTree.data
                projectClassifyDao.insertList(projectList)
                state.postValue(PlaySuccess(projectList))
            } else {
                state.postValue(PlayError(NetworkErrorException("")))
            }
        }
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
        Log.e("ZHUJIANG521", "getProject: $query")
        state.postValue(PlayLoading)
        if (!NetworkUtils.isConnected()) {
            showToast(R.string.no_network)
            state.postValue(PlayError(NetworkErrorException("网络未🔗")))
            return
        }
        Log.e("ZHUJIANG521", "getProject111: $query")
        val res: java.util.ArrayList<Article>
        if (query.page == 0) {
            res = arrayListOf()
            val dataStore = DataStoreUtils
            val articleListForChapterId =
                articleListDao.getArticleListForChapterId(PROJECT, query.cid)
            var downArticleTime = 0L
            dataStore.readLongFlow(DOWN_PROJECT_ARTICLE_TIME, System.currentTimeMillis()).first {
                downArticleTime = it
                true
            }
            if (articleListForChapterId.isNotEmpty() && downArticleTime > 0 && downArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh) {
                res.addAll(articleListForChapterId)
                state.postValue(PlaySuccess(res))
                value.postValue(res)
                Log.e("ZHUJIANG521", "getProject000: ${res.size}")
            } else {
                val projectTree = PlayAndroidNetwork.getProject(query.page, query.cid)
                if (projectTree.errorCode == 0) {
                    if (articleListForChapterId.isNotEmpty() && articleListForChapterId[0].link == projectTree.data.datas[0].link && !query.isRefresh) {
                        state.postValue(PlaySuccess(articleListForChapterId))
                    } else {
                        projectTree.data.datas.forEach {
                            it.localType = PROJECT
                        }
                        dataStore.saveLongData(
                            DOWN_PROJECT_ARTICLE_TIME,
                            System.currentTimeMillis()
                        )
                        if (query.isRefresh) {
                            articleListDao.deleteAll(PROJECT, query.cid)
                        }
                        articleListDao.insertList(projectTree.data.datas)
                        res.addAll(projectTree.data.datas)
                        state.postValue(PlaySuccess(res))
                        value.postValue(res)
                        Log.e("ZHUJIANG521", "getProject111: ${res.size}")
                    }
                } else {
                    value.postValue(res)
                    state.postValue(PlayError(NetworkErrorException("")))
                }
            }
        } else {
            res = value.value ?: arrayListOf()
            val projectTree = PlayAndroidNetwork.getProject(query.page, query.cid)
            if (projectTree.errorCode == 0) {
                res.addAll(projectTree.data.datas)
                state.postValue(PlaySuccess(res))
                value.postValue(res)
            } else {
                value.postValue(res)
                state.postValue(PlayError(NetworkErrorException("")))
            }
        }
    }

}