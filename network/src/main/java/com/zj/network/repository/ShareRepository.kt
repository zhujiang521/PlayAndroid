package com.zj.network.repository

import com.zj.network.base.PlayAndroidNetwork

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/23
 * 描述：PlayAndroid
 *
 */
object ShareRepository {

    fun getMyShareList(page: Int) = fires { PlayAndroidNetwork.getMyShareList(page) }

    fun getShareList(cid: Int, page: Int) = fires { PlayAndroidNetwork.getShareList(cid, page) }

    fun deleteMyArticle(cid: Int) = fires { PlayAndroidNetwork.deleteMyArticle(cid) }

    fun shareArticle(title: String, link: String) =
        fires { PlayAndroidNetwork.shareArticle(title, link) }

}