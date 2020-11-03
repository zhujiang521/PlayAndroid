package com.zj.network.repository

import android.content.Context
import com.blankj.utilcode.util.SPUtils
import com.zj.core.util.Preference
import com.zj.model.pojo.QueryArticle
import com.zj.network.base.PlayAndroidNetwork
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.PROJECT

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
class ProjectRepository(context: Context) {

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
            val articleListForChapterId =
                articleListDao.getArticleListForChapterId(PROJECT, query.cid)
            val spUtils = SPUtils.getInstance()
            val downArticleTime by Preference(DOWN_PROJECT_ARTICLE_TIME, System.currentTimeMillis())
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
                        spUtils.put(DOWN_PROJECT_ARTICLE_TIME, System.currentTimeMillis())
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