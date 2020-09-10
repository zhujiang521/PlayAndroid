package com.zj.play.view.official

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
class OfficialRepository(private val projectClassifyDao: ProjectClassifyDao) {

    /**
     * 获取公众号标题列表
     */
    fun getProjectTree() = fire {
        val projectClassifyLists = projectClassifyDao.getAllOfficial()
        if (projectClassifyLists.isNotEmpty()) {
            Result.success(projectClassifyLists)
        } else {
            val projectTree = PlayAndroidNetwork.getWxArticleTree()
            if (projectTree.errorCode == 0) {
                val projectList = projectTree.data
                projectClassifyDao.insertList(projectList)
                Result.success(projectList)
            } else {
                Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
            }
        }

    }

}