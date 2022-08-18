package com.zj.core

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.zj.core.util.DataStoreUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 全局的API接口。
 *
 */
@SuppressLint("StaticFieldLeak")
object Play {
    private const val TAG = "Play"
    private const val USERNAME = "username"
    private const val NICE_NAME = "nickname"
    private const val IS_LOGIN = "isLogin"
    private lateinit var dataStore: DataStoreUtils

    /**
     * 获取全局Context，在代码的任意位置都可以调用，随时都能获取到全局Context对象。
     *
     * @return 全局Context对象。
     */
    var context: Context? = null
        private set

    /**
     * 初始化接口。这里会进行应用程序的初始化操作，一定要在代码执行的最开始调用。
     *
     * @param c Context参数，注意这里要传入的是Application的Context，千万不能传入Activity或者Service的Context。
     */
    fun initialize(c: Context?) {
        if (c == null) {
            Log.w(TAG, "initialize: context is null")
            return
        }
        context = c
        context?.apply {
            dataStore = DataStoreUtils.init(this)
        }
    }

    /**
     * 判断用户是否已登录。
     *
     * @return 已登录返回true，未登录返回false。
     */
    fun isLogin(): Flow<Boolean> {
        return if (::dataStore.isInitialized) {
            dataStore.readBooleanFlow(IS_LOGIN)
        } else {
            flow {
                emit(false)
            }
        }
    }

    fun isLoginResult(): Boolean {
        return if (::dataStore.isInitialized) {
            dataStore.readBooleanData(IS_LOGIN)
        } else {
            false
        }
    }

    fun setLogin(isLogin: Boolean) {
        dataStore.saveSyncBooleanData(IS_LOGIN, isLogin)
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