package com.zj.play.network

import android.util.Log
import androidx.lifecycle.liveData

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
object Repository {

    fun getLogin(username: String, password: String) = fire {
        val projectTree = PlayAndroidNetwork.getLogin(username, password)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

    fun getRegister(username: String, password: String, repassword: String) = fire {
        val projectTree = PlayAndroidNetwork.getRegister(username, password, repassword)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

    fun getLogout() = fire {
        val projectTree = PlayAndroidNetwork.getLogout()
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

}

fun <T> fire(block: suspend () -> Result<T>) =
    liveData {
        val result = try {
            block()
        } catch (e: Exception) {
            Log.e("ZHUJIANG哈哈哈", e.toString())
            Result.failure<T>(e)
        }
        emit(result)
    }