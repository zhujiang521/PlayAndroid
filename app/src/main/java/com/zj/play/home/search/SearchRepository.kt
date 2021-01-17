package com.zj.play.home.search

import android.app.Application
import com.zj.model.room.PlayDatabase
import com.zj.network.base.PlayAndroidNetwork
import com.zj.play.main.login.fire
import com.zj.play.main.login.fires
import javax.inject.Inject

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
class SearchRepository @Inject constructor(application: Application) {

    private val hotKeyDao = PlayDatabase.getDatabase(application).hotKeyDao()

    /**
     * 获取搜索热词
     */
    fun getHotKey() = fire {
        val hotKeyList = hotKeyDao.getHotKeyList()
        if (hotKeyList.isNotEmpty()) {
            Result.success(hotKeyList)
        } else {
            val projectTree = PlayAndroidNetwork.getHotKey()
            if (projectTree.errorCode == 0) {
                val hotKeyLists = projectTree.data
                hotKeyDao.insertList(hotKeyLists)
                Result.success(hotKeyLists)
            } else {
                Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
            }
        }
    }

    /**
     * 获取搜索结果
     */
    fun getQueryArticleList(page: Int, k: String) = fires {
        PlayAndroidNetwork.getQueryArticleList(page, k)
    }

}