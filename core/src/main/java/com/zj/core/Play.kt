package com.zj.core

import android.annotation.SuppressLint
import android.content.Context
import com.blankj.utilcode.util.SPUtils
import com.zj.core.util.Preference.Companion.setContext

/**
 * 全局的API接口。
 *
 */
object Play {
    private const val USERNAME = "username"
    private const val NICE_NAME = "nickname"
    private const val IS_LOGIN = "isLogin"

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
        setContext(context!!)
    }

    /**
     * 判断用户是否已登录。
     *
     * @return 已登录返回true，未登录返回false。
     */
    var isLogin: Boolean
        get() = SPUtils.getInstance().getBoolean(IS_LOGIN, false)
        set(b) {
            SPUtils.getInstance().put(IS_LOGIN, b)
        }

    /**
     * 返回当前应用的包名。
     */
    val packageName: String
        get() = context!!.packageName

    /**
     * 注销用户登录。
     */
    fun logout() {
        SPUtils.getInstance().clear()
    }

    fun setUserInfo(nickname: String?, username: String?) {
        SPUtils.getInstance().put(NICE_NAME, nickname)
        SPUtils.getInstance().put(USERNAME, username)
    }

    val nickName: String
        get() = SPUtils.getInstance().getString(NICE_NAME, "")
    val username: String
        get() = SPUtils.getInstance().getString(USERNAME, "")
}