package com.zj.network.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.zj.core.util.showToast
import com.zj.model.model.BaseModel
import com.zj.network.base.PlayAndroidNetwork

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
object AccountRepository {

    suspend fun getLogin(username: String, password: String) =
        PlayAndroidNetwork.getLogin(username, password)

    suspend fun getRegister(username: String, password: String, repassword: String) =
        PlayAndroidNetwork.getRegister(username, password, repassword)

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
                Log.e(
                    "ZHUJIANG哈哈fires",
                    "fires: response status is ${baseModel.errorCode}  msg is ${baseModel.errorMsg}"
                )
                showToast(baseModel.errorMsg)
                Result.failure(RuntimeException(baseModel.errorMsg))
            }
        } catch (e: Exception) {
            Log.e("ZHUJIANG哈哈哈fires", e.toString())
            Result.failure(e)
        }
        emit(result)
    }

fun <T> fire(block: suspend () -> Result<T>) =
    liveData {
        val result = try {
            block()
        } catch (e: Exception) {
            Log.e("ZHUJIANG哈哈哈fire", e.toString())
            Result.failure(e)
        }
        emit(result)
    }