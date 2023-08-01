package com.zj.play

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.zj.utils.DataStoreUtils

/**
 * 登录状态
 */
val loginState: MutableState<Boolean> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    mutableStateOf(getDefaultLogin())
}

/**
 * 获取当前默认登录状态
 */
fun getDefaultLogin(): Boolean = DataStoreUtils.getSyncData(Play.IS_LOGIN, false)

/**
 * 全局的API接口。
 *
 */
object Play {
    private const val USERNAME = "username"
    private const val NICE_NAME = "nickname"
    const val IS_LOGIN = "isLogin"
    private var dataStore = DataStoreUtils

    /**
     * 注销用户登录。
     */
    fun logout() {
        loginState.value = false
        dataStore.putSyncData(IS_LOGIN, false)
    }

    fun setUserInfo(nickname: String, username: String) {
        dataStore.saveSyncStringData(NICE_NAME, nickname)
        dataStore.saveSyncStringData(USERNAME, username)
    }

    val nickName: String
        get() = dataStore.readStringData(NICE_NAME)
    val username: String
        get() = dataStore.readStringData(USERNAME)
}