package com.zj.play.profile.history

import android.app.Application
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.HISTORY
import com.zj.play.main.login.fire
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
@ActivityScoped
class BrowseHistoryRepository @Inject constructor(val application: Application) {

    private val browseHistoryDao = PlayDatabase.getDatabase(application).browseHistoryDao()

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