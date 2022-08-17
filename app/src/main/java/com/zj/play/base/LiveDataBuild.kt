package com.zj.play.base

import androidx.lifecycle.liveData
import com.zj.model.model.BaseModel
import kotlinx.coroutines.Dispatchers

fun <T> liveDataModel(block: suspend () -> BaseModel<T>) =
    liveData(Dispatchers.IO) {
        val result = try {
            val baseModel = block()
            if (baseModel.errorCode == 0) {
                val model = baseModel.data
                Result.success(model)
            } else {
                Result.failure(RuntimeException(baseModel.errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

fun <T> liveDataFire(block: suspend () -> Result<T>) =
    liveData(Dispatchers.IO) {
        val result = try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result)
    }