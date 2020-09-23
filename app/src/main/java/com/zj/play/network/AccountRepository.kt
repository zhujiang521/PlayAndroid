package com.zj.play.network

import android.util.Log
import androidx.lifecycle.liveData
import com.zj.play.model.BaseModel

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
object Repository {

    fun getLogin(username: String, password: String) = fires {
        PlayAndroidNetwork.getLogin(username, password)
    }

    fun getRegister(username: String, password: String, repassword: String) =
        fires { PlayAndroidNetwork.getRegister(username, password, repassword) }

    fun getLogout() = fires { PlayAndroidNetwork.getLogout() }

}


fun <T> fires(block: suspend () -> BaseModel<T>) =
    liveData {
        val result = try {
            val baseModel = block()
            if (baseModel.errorCode == 0) {
                val model = baseModel.data
                Result.success(model)
            } else {
                Result.failure(RuntimeException("response status is ${baseModel.errorCode}  msg is ${baseModel.errorMsg}"))
            }
        } catch (e: Exception) {
            Log.e("ZHUJIANG哈哈哈fires", e.toString())
            Result.failure(e)
        }
        emit(result)
    }

fun <T> fire(block: suspend () -> Result<T>) =
    liveData {
//        val result = kotlin.runCatching {
//            block()
//        }
        val result = try {
            block()
        } catch (e: Exception) {
            Log.e("ZHUJIANG哈哈哈fire", e.toString())
            Result.failure(e)
        }
        emit(result)
    }