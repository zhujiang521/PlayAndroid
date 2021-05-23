package com.zj.play

import com.zj.play.logic.utils.DataStoreUtils

/**
 * 全局的API接口。
 *
 */
object Play {
    private const val USERNAME = "username"
    private const val NICE_NAME = "nickname"
    private const val IS_LOGIN = "isLogin"
    private var dataStore = DataStoreUtils

    /**
     * 初始化接口。这里会进行应用程序的初始化操作，一定要在代码执行的最开始调用。
     *
     */
    fun initialize(dataStoreUtils: DataStoreUtils) {
        dataStore = dataStoreUtils
    }

    /**
     * 判断用户是否已登录。
     *
     * @return 已登录返回true，未登录返回false。
     */
    var isLogin: Boolean
        get() = dataStore.readBooleanData(IS_LOGIN)
        set(b) {
            dataStore.saveSyncBooleanData(IS_LOGIN, b)
        }

    /**
     * 注销用户登录。
     */
    fun logout() {
        dataStore.clearSync()
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