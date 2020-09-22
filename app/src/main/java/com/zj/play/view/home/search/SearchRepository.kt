package com.zj.play.view.home.search

import android.content.Context
import com.zj.play.network.PlayAndroidNetwork
import com.zj.play.network.fire
import com.zj.play.network.fires
import com.zj.play.room.PlayDatabase

/**
 * 版权：联想 版权所有
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
                val bannerList = projectTree.data
                hotKeyDao.insertList(bannerList)
                Result.success(bannerList)
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