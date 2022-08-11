package com.zj.play.logic.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.google.gson.JsonSyntaxException
import com.zj.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/3/24
 * 描述：PlayAndroid
 *
 */

abstract class BaseArticlePagingRepository {
    companion object {
        const val PAGE_SIZE = 15
    }

    abstract fun getPagingData(query: Query): Flow<PagingData<ArticleModel>>
    
      @OptIn(DelicateCoroutinesApi::class)
      fun <T> http(
          scope: CoroutineScope = GlobalScope,
          dispatchers: CoroutineDispatcher = Dispatchers.Main,
          request: suspend CoroutineScope.() -> BaseModel<T>,
          state: MutableLiveData<PlayState<T>>
    ): Job {
        return scope.launch(dispatchers) {
            try {
                val response = request()
                if (response.errorCode == 0) {
                    val bannerList = response.data
                    state.postValue(PlaySuccess(bannerList))
                } else {
                    state.postValue(PlayError(RuntimeException("response status is ${response.errorCode}  msg is ${response.errorMsg}")))
                }

            } catch (e: Exception) {
                state.postValue(PlayError(RuntimeException(handleException(e))))
            }
        }

    }

    /**
     * 异常处理
     */
    private fun handleException(e: Exception): String {
        val msg = when (e) {
            is CancellationException -> {
                "取消异常"   // 取消异常
            }
            is JsonSyntaxException -> {
                "解析异常"
            }
            is HttpException -> {
                "url不存在"
            }
            // .....TODO 其他异常
            else -> {
                "网络异常"
            }
        }

        return msg
    }
}
