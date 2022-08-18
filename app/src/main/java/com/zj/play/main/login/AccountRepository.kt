package com.zj.play.main.login

import com.zj.network.base.PlayAndroidNetwork
import com.zj.play.base.liveDataModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
@ActivityRetainedScoped
class AccountRepository @Inject constructor() {

    suspend fun getLogin(username: String, password: String) =
        PlayAndroidNetwork.getLogin(username, password)

    suspend fun getRegister(username: String, password: String, repassword: String) =
        PlayAndroidNetwork.getRegister(username, password, repassword)

    fun getLogout() = liveDataModel { PlayAndroidNetwork.getLogout() }

}