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

    private val projectClassifyDao = PlayDatabase.getDatabase(application).projectClassifyDao()

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

}