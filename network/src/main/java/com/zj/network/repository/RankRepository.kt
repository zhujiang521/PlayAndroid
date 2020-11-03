package com.zj.network.repository

import com.zj.network.base.PlayAndroidNetwork

/**
 * 版权：Zhujiang 个人版权
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
    fun getRankList(page: Int) = fires { PlayAndroidNetwork.getRankList(page) }

    /**
     * 获取个人积分获取列表
     *
     * @param page 页码
     */
    fun getUserRank(page: Int) = fires { PlayAndroidNetwork.getUserRank(page) }

    /**
     * 获取个人积分信息
     */
    fun getUserInfo() = fires { PlayAndroidNetwork.getUserInfo() }


}