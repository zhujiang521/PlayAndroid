package com.zj.play.compose.repository

import android.accounts.NetworkErrorException
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.zj.core.util.DataStoreUtils
import com.zj.core.util.showToast
import com.zj.model.model.ArticleList
import com.zj.model.model.BaseModel
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.OFFICIAL
import com.zj.model.room.entity.ProjectClassify
import com.zj.play.R
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlayState
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.home.DOWN_OFFICIAL_ARTICLE_TIME
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
abstract class ArticleRepository(application: Application) {

    private val projectClassifyDao = PlayDatabase.getDatabase(application).projectClassifyDao()
    private val articleListDao = PlayDatabase.getDatabase(application).browseHistoryDao()

    /**
     * 获取公众号标题列表
     */
    suspend fun getTree(state: MutableLiveData<PlayState>, isRefresh: Boolean) {
        state.postValue(PlayLoading)
        if (!NetworkUtils.isConnected()) {
            showToast(R.string.no_network)
            state.postValue(PlayError(NetworkErrorException("网络未🔗")))
            return
        }
        val projectClassifyLists = if (getFlag() == DOWN_OFFICIAL_ARTICLE_TIME) {
            projectClassifyDao.getAllOfficial()
        } else {
            projectClassifyDao.getAllProject()
        }
        if (projectClassifyLists.isNotEmpty() && !isRefresh) {
            state.postValue(PlaySuccess(projectClassifyLists))
        } else {
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
        query: QueryArticle
    ) {
        state.postValue(PlayLoading)
        if (!NetworkUtils.isConnected()) {
            showToast(R.string.no_network)
            state.postValue(PlayError(NetworkErrorException("网络未🔗")))
            return
        }
        val res: java.util.ArrayList<Article>
        if (query.page == 0) {
            res = arrayListOf()
            val dataStore = DataStoreUtils
            val articleListForChapterId =
                articleListDao.getArticleListForChapterId(OFFICIAL, query.cid)
            var downArticleTime = 0L
            dataStore.readLongFlow(getFlag(), System.currentTimeMillis()).first {
                downArticleTime = it
                true
            }
            if (articleListForChapterId.isNotEmpty() && downArticleTime > 0 && downArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh) {
                res.addAll(articleListForChapterId)
                state.postValue(PlaySuccess(res))
                value.postValue(res)
            } else {
                val projectTree = getArticleList(query.page, query.cid)
                if (projectTree.errorCode == 0) {
                    if (articleListForChapterId.isNotEmpty() && articleListForChapterId[0].link == projectTree.data.datas[0].link && !query.isRefresh) {
                        res.addAll(articleListForChapterId)
                        state.postValue(PlaySuccess(res))
                        value.postValue(res)
                    } else {
                        projectTree.data.datas.forEach {
                            it.localType = getLocalType()
                        }
                        DataStoreUtils.saveLongData(
                            getFlag(),
                            System.currentTimeMillis()
                        )
                        if (query.isRefresh) {
                            articleListDao.deleteAll(getLocalType(), query.cid)
                        }
                        articleListDao.insertList(projectTree.data.datas)
                        res.addAll(projectTree.data.datas)
                        state.postValue(PlaySuccess(res))
                        value.postValue(res)
                    }
                } else {
                    state.postValue(PlayError(NetworkErrorException("")))
                    value.postValue(res)
                }
            }
        } else {
            res = value.value ?: arrayListOf()
            val projectTree = getArticleList(query.page, query.cid)
            if (projectTree.errorCode == 0) {
                res.addAll(projectTree.data.datas)
                state.postValue(PlaySuccess(res))
                value.postValue(res)
            } else {
                state.postValue(PlayError(NetworkErrorException("")))
            }
        }

    }

}