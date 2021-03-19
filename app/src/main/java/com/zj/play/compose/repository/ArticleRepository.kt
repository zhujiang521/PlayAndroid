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
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.ProjectClassify
import com.zj.play.R
import com.zj.play.compose.model.*
import kotlinx.coroutines.flow.first

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
abstract class ArticleRepository(application: Application) {

    companion object {
        private const val TAG = "ArticleRepository"
    }

    private val projectClassifyDao = PlayDatabase.getDatabase(application).projectClassifyDao()
    private val articleListDao = PlayDatabase.getDatabase(application).browseHistoryDao()

    /**
     * 获取公众号标题列表
     */
    suspend fun getTree(state: MutableLiveData<PlayState>, isRefresh: Boolean) {
        state.postValue(PlayLoading)
        val projectClassifyLists = if (getFlag() == DOWN_OFFICIAL_ARTICLE_TIME) {
            projectClassifyDao.getAllOfficial()
        } else {
            projectClassifyDao.getAllProject()
        }
        if (projectClassifyLists.isNotEmpty() && !isRefresh) {
            state.postValue(PlaySuccess(projectClassifyLists))
        } else {
            if (!NetworkUtils.isConnected()) {
                showToast(R.string.no_network)
                state.postValue(PlayError(NetworkErrorException("网络未🔗")))
                return
            }
            val projectTree = getArticleTree()
            if (projectTree.errorCode == 0) {
                val projectList = projectTree.data
                projectClassifyDao.insertList(projectList)
                state.postValue(PlaySuccess(projectList))
            } else {
                state.postValue(PlayError(NetworkErrorException("")))
            }
        }

    }

    abstract suspend fun getArticleTree(): BaseModel<List<ProjectClassify>>

    abstract suspend fun getFlag(): String

    abstract suspend fun getLocalType(): Int

    abstract suspend fun getArticleList(page: Int, cid: Int): BaseModel<ArticleList>

    /**
     * 获取文章列表
     * @param query 查询
     */
    suspend fun getArticleList(
        state: MutableLiveData<PlayState>,
        value: MutableLiveData<ArrayList<Article>>,
        query: QueryArticle,
        isLoad: Boolean
    ) {
        if (!isLoad) {
            state.postValue(PlayLoading)
        } else {
            if (!NetworkUtils.isConnected()) {
                showToast(R.string.no_network)
                return
            }
        }
        Log.e(TAG, "getArticleList: isLoad:$isLoad   query:$query")
        Log.e(TAG, "getArticleList: 111")
        val res: java.util.ArrayList<Article>
        if (query.page == 0) {
            Log.e(TAG, "getArticleList: 222")
            res = arrayListOf()
            val dataStore = DataStoreUtils
            val articleListForChapterId =
                articleListDao.getArticleListForChapterId(getLocalType(), query.cid)
            var downArticleTime = 0L
            dataStore.readLongFlow(getFlag(), System.currentTimeMillis()).first {
                downArticleTime = it
                true
            }
            if (articleListForChapterId.isNotEmpty() && downArticleTime > 0 && downArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh) {
                res.addAll(articleListForChapterId)
                state.postValue(PlaySuccess(res))
                value.postValue(res)
                Log.e(TAG, "getArticleList: 222 : ${res.size}")
            } else {
                getFirstArticleList(state, value, query, res, articleListForChapterId)
                Log.e(TAG, "getArticleList: 333 : ${res.size}")
            }
        } else {
            Log.e(TAG, "getArticleList: 444")
            res = value.value ?: arrayListOf()
            val articleList = getArticleList(query.page, query.cid)
            if (articleList.errorCode == 0) {
                if (articleList.data.datas.isNotEmpty() && !res.contains(articleList.data.datas[0])) {
                    res.addAll(articleList.data.datas)
                }
                value.postValue(res)
                state.postValue(PlaySuccess(res))
                Log.e(TAG, "getArticleList: 555 : ${res.size}")
            } else {
                Log.e(TAG, "getArticleList: 666 : ${res.size}")
                state.postValue(PlayError(NetworkErrorException("")))
            }
        }

    }

    private suspend fun getFirstArticleList(
        state: MutableLiveData<PlayState>,
        value: MutableLiveData<java.util.ArrayList<Article>>,
        query: QueryArticle,
        res: java.util.ArrayList<Article>,
        articleListForChapterId: List<Article>
    ) {
        if (!NetworkUtils.isConnected()) {
            showToast(R.string.no_network)
            state.postValue(PlayError(NetworkErrorException("网络未🔗")))
            return
        }
        Log.e(TAG, "getFirstArticleList: 777")
        val articleList = getArticleList(query.page, query.cid)
        if (articleList.errorCode == 0) {
            if (articleListForChapterId.isNotEmpty() && articleListForChapterId[0].link == articleList.data.datas[0].link && !query.isRefresh) {
                res.addAll(articleListForChapterId)
                state.postValue(PlaySuccess(res))
                value.postValue(res)
                Log.e(TAG, "getFirstArticleList: 888:${res.size}")
            } else {
                articleList.data.datas.forEach {
                    it.localType = getLocalType()
                }
                DataStoreUtils.saveLongData(
                    getFlag(),
                    System.currentTimeMillis()
                )
                if (query.isRefresh) {
                    articleListDao.deleteAll(getLocalType(), query.cid)
                }
                articleListDao.insertList(articleList.data.datas)
                res.addAll(articleList.data.datas)
                state.postValue(PlaySuccess(res))
                value.postValue(res)
                Log.e(TAG, "getFirstArticleList: 999:${res.size}")
            }
        } else {
            state.postValue(PlayError(NetworkErrorException("")))
            value.postValue(res)
            Log.e(TAG, "getFirstArticleList: 101010:${res.size}")
        }
    }

}