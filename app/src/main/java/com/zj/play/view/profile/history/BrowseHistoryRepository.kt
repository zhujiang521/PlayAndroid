package com.zj.play.view.profile.history

import android.content.Context
import android.util.Log
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
class BrowseHistoryRepository(context: Context) {


    private val browseHistoryDao = PlayDatabase.getDatabase(context).browseHistoryDao()

    /**
     * 获取公众号标题列表
     */
    fun getBrowseHistory(page: Int) = fire {
        val projectClassifyLists = browseHistoryDao.getArticleList((page - 1) * 20)
        if (projectClassifyLists.isNotEmpty()) {
            Result.success(projectClassifyLists)
        } else {
            Result.failure(RuntimeException("response status is "))
        }

    }

}