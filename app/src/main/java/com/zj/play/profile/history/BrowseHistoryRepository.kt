package com.zj.play.profile.history

import android.content.Context
import com.zj.network.repository.fire
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.HISTORY

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
     * 获取历史记录列表
     */
    fun getBrowseHistory(page: Int) = fire {
        val projectClassifyLists = browseHistoryDao.getHistoryArticleList((page - 1) * 20,HISTORY)
        if (projectClassifyLists.isNotEmpty()) {
            Result.success(projectClassifyLists)
        } else {
            Result.failure(RuntimeException("response status is "))
        }

    }

}