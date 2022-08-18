package com.zj.play.profile.share

import com.zj.network.base.PlayAndroidNetwork
import com.zj.play.base.liveDataModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/23
 * 描述：PlayAndroid
 *
 */
@ActivityRetainedScoped
class ShareRepository @Inject constructor() {

    fun getMyShareList(page: Int) = liveDataModel { PlayAndroidNetwork.getMyShareList(page) }

    fun getShareList(cid: Int, page: Int) = liveDataModel { PlayAndroidNetwork.getShareList(cid, page) }

    fun deleteMyArticle(cid: Int) = liveDataModel { PlayAndroidNetwork.deleteMyArticle(cid) }

    fun shareArticle(title: String, link: String) =
        liveDataModel { PlayAndroidNetwork.shareArticle(title, link) }

}