package com.zj.play.ui.page.system

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zj.model.AndroidSystemModel
import com.zj.model.PlayError
import com.zj.model.PlayState
import com.zj.model.PlaySuccess
import com.zj.model.Query
import com.zj.network.PlayAndroidNetwork
import com.zj.play.R
import com.zj.play.logic.paging.SystemPagingSource
import com.zj.play.logic.repository.BaseArticlePagingRepository
import com.zj.utils.NetworkUtils.isConnected
import com.zj.utils.showToast

class SystemRepository : BaseArticlePagingRepository() {

    override fun getPagingData(query: Query) = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        SystemPagingSource(query.cid)
    }.flow

    suspend fun getAndroidSystem(
        state: MutableState<PlayState<List<AndroidSystemModel>>>,
        context: Context
    ) {
        if (!context.isConnected()) {
            showToast(context, R.string.no_network)
            state.value = PlayError(NetworkErrorException())
            return
        }
        val response = PlayAndroidNetwork.getAndroidSystem()
        if (response.errorCode == 0) {
            val data = response.data
            data.sortedBy {
                it.id
            }
            state.value = PlaySuccess(data)
        } else {
            state.value =
                PlayError(RuntimeException("response status is ${response.errorCode}  msg is ${response.errorMsg}"))
        }
    }

}