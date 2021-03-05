package com.zj.play.main.login

import android.util.Log
import androidx.lifecycle.liveData
import com.zj.core.util.showToast
import com.zj.model.model.BaseModel
import com.zj.network.base.PlayAndroidNetwork
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

class AccountRepository {

    suspend fun getLogin(username: String, password: String) =
        PlayAndroidNetwork.getLogin(username, password)

    suspend fun getRegister(username: String, password: String, repassword: String) =
        PlayAndroidNetwork.getRegister(username, password, repassword)

    suspend fun getLogout() = PlayAndroidNetwork.getLogout()

}

private const val TAG = "AccountRepository"

fun <T> fires(block: suspend () -> BaseModel<T>) =
    liveData {
        val result = try {
            val baseModel = block()
            if (baseModel.errorCode == 0) {
                val model = baseModel.data
                Result.success(model)
            } else {
                Log.e(
                    TAG,
                    "fires: response status is ${baseModel.errorCode}  msg is ${baseModel.errorMsg}"
                )
                showToast(baseModel.errorMsg)
                Result.failure(RuntimeException(baseModel.errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Result.failure(e)
        }
        emit(result)
    }

fun <T> fire(block: suspend () -> Result<T>) =
    liveData {
        val result = try {
            block()
        } catch (e: Exception) {
            Log.e(TAG, "fire $e")
            Result.failure(e)
        }
        emit(result)
    }

fun <T> composeFire(block: suspend () -> T) =
    liveData {
        emit(block())
    }

fun <T> composeFires(block: suspend () -> BaseModel<T>) =
    liveData {
        val result = try {
            val baseModel = block()
            if (baseModel.errorCode == 0) {
                val model = baseModel.data
                model
            } else {
                Log.e(
                    TAG,
                    "fires: response status is ${baseModel.errorCode}  msg is ${baseModel.errorMsg}"
                )
                showToast(baseModel.errorMsg)
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            null
        }
        emit(result)
    }