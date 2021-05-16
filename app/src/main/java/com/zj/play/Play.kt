package com.zj.play

import android.annotation.SuppressLint
import android.content.Context
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
     * 获取全局Context，在代码的任意位置都可以调用，随时都能获取到全局Context对象。
     *
     * @return 全局Context对象。
     */
    @SuppressLint("StaticFieldLeak")
    var context: Context? = null
        private set

    /**
     * 初始化接口。这里会进行应用程序的初始化操作，一定要在代码执行的最开始调用。
     *
     * @param c Context参数，注意这里要传入的是Application的Context，千万不能传入Activity或者Service的Context。
     */
    fun initialize(c: Context?) {
        context = c
        DataStoreUtils.init(context!!)

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