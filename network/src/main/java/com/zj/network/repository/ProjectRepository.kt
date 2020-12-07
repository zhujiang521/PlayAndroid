package com.zj.network.repository

import android.content.Context
import com.zj.core.util.DataStoreUtils
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.PROJECT
import com.zj.network.base.PlayAndroidNetwork
import kotlinx.coroutines.flow.first

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
class ProjectRepository(private val context: Context) {

    private val projectClassifyDao = PlayDatabase.getDatabase(context).projectClassifyDao()
    private val articleListDao = PlayDatabase.getDatabase(context).browseHistoryDao()

    /**
     * 获取项目标题列表
     */
    fun getProjectTree(isRefresh: Boolean) = fire {
        val projectClassifyLists = projectClassifyDao.getAllProject()
        if (projectClassifyLists.isNotEmpty() && !isRefresh) {
            Result.success(projectClassifyLists)
        } else {
            val projectTree = PlayAndroidNetwork.getProjectTree()
            if (projectTree.errorCode == 0) {
                val projectList = projectTree.data
                projectClassifyDao.insertList(projectList)
                Result.success(projectList)
            } else {
                Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
            }
        }
    }

    /**
     * 获取项目具体文章列表
     * @param page 页码
     * @param cid 项目id
     */
    fun getProject(query: QueryArticle) = fire {
        if (query.page == 1) {
            val dataStore = DataStoreUtils
            val articleListForChapterId =
                articleListDao.getArticleListForChapterId(PROJECT, query.cid)
            var downArticleTime = 0L
            dataStore.readLongFlow(DOWN_PROJECT_ARTICLE_TIME, System.currentTimeMillis()).first {
                downArticleTime = it
                true
            }
            if (articleListForChapterId.isNotEmpty() && downArticleTime > 0 && downArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh) {
                Result.success(articleListForChapterId)
            } else {
                val projectTree = PlayAndroidNetwork.getProject(query.page, query.cid)
                if (projectTree.errorCode == 0) {
                    if (articleListForChapterId.isNotEmpty() && articleListForChapterId[0].link == projectTree.data.datas[0].link && !query.isRefresh) {
                        Result.success(articleListForChapterId)
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
                        Result.success(projectTree.data.datas)
                    }
                } else {
                    Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
                }
            }
        } else {
            val projectTree = PlayAndroidNetwork.getProject(query.page, query.cid)
            if (projectTree.errorCode == 0) {
                Result.success(projectTree.data.datas)
            } else {
                Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
            }
        }
    }

}