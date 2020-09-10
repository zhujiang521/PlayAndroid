package com.zj.play.view.home.search

import com.zj.play.network.PlayAndroidNetwork
import com.zj.play.network.fire

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
object SearchRepository {

    /**
     * 获取搜索热词
     */
    fun getHotKey() = fire {
        val projectTree = PlayAndroidNetwork.getHotKey()
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

    /**
     * 获取搜索结果
     */
    fun getQueryArticleList(page: Int, k: String) = fire {
        val projectTree = PlayAndroidNetwork.getQueryArticleList(page, k)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

}