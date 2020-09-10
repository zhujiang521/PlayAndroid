package com.zj.play.view.project

import android.content.Context
import com.zj.play.network.PlayAndroidNetwork
import com.zj.play.network.fire
import com.zj.play.room.PlayDatabase

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
class ProjectRepository(context: Context) {

    private val projectClassifyDao = PlayDatabase.getDatabase(context).projectClassifyDao()

    /**
     * 获取项目标题列表
     */
    fun getProjectTree() = fire {
        val projectClassifyLists = projectClassifyDao.getAllProject()
        if (projectClassifyLists.isNotEmpty()) {
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
    fun getProject(page: Int, cid: Int) = fire {
        val projectTree = PlayAndroidNetwork.getProject(page, cid)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList.datas)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

}