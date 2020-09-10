package com.zj.play.view.project

import android.util.Log
import com.zj.play.network.PlayAndroidNetwork
import com.zj.play.network.fire
import com.zj.play.room.dao.ProjectClassifyDao

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
class ProjectRepository(private val projectClassifyDao: ProjectClassifyDao) {

    /**
     * 获取项目标题列表
     */
    fun getProjectTree() = fire {
        val projectClassifyLists = projectClassifyDao.getAll()
        if (projectClassifyLists.isNotEmpty()) {
            Log.e("ZHUJIANG", "222getProjectTree: $projectClassifyLists")
            Result.success(projectClassifyLists)
        } else {
            val projectTree = PlayAndroidNetwork.getProjectTree()
            if (projectTree.errorCode == 0) {
                val projectList = projectTree.data
                Log.e("ZHUJIANG", "111getProjectTree: $projectList")
                projectClassifyDao.insertList(projectList)
                Result.success(projectList)
            } else {
                Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
            }
        }

    }

}