package com.zj.network.repository

import android.content.Context
import com.zj.model.room.PlayDatabase
import com.zj.network.base.PlayAndroidNetwork

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
class SearchRepository(context: Context) {

    private val hotKeyDao = PlayDatabase.getDatabase(context).hotKeyDao()

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