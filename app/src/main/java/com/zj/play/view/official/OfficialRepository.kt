package com.zj.play.view.official

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
class OfficialRepository(context: Context) {

    private val projectClassifyDao = PlayDatabase.getDatabase(context).projectClassifyDao()

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

    /**
     * 获取具体公众号文章列表
     * @param page 页码
     * @param cid 公众号id
     */
    fun getWxArticle(page: Int, cid: Int) = fire {
        val projectTree = PlayAndroidNetwork.getWxArticle(page, cid)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList.datas)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }


}