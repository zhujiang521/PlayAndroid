package com.zj.play.network

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
object RankRepository {

    /**
     * 获取排行榜列表
     *
     * @param page 页码
     */
    fun getRankList(page: Int) = fire {
        val projectTree = PlayAndroidNetwork.getRankList(page)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

    /**
     * 获取个人积分获取列表
     *
     * @param page 页码
     */
    fun getUserRank(page: Int) = fire {
        val projectTree = PlayAndroidNetwork.getUserRank(page)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

    /**
     * 获取个人积分信息
     */
    fun getUserInfo() = fire {
        val projectTree = PlayAndroidNetwork.getUserInfo()
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }


}