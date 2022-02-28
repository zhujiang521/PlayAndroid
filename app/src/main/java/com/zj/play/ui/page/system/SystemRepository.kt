package com.zj.play.ui.page.system

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zj.play.R
import com.zj.play.logic.paging.SystemPagingSource
import com.zj.play.logic.repository.BaseArticlePagingRepository
import com.zj.model.*
import com.zj.network.PlayAndroidNetwork
import com.zj.utils.NetworkUtils
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
        state: MutableLiveData<PlayState<List<AndroidSystemModel>>>,
        context: Context
    ) {
        if (!NetworkUtils.isConnected(context)) {
            showToast(context, R.string.no_network)
            state.postValue(PlayError(NetworkErrorException()))
            return
        }
        state.postValue(PlayLoading)
        val response = PlayAndroidNetwork.getAndroidSystem()
        if (response.errorCode == 0) {
            val data = response.data
            state.postValue(PlaySuccess(data))
        } else {
            state.postValue(PlayError(RuntimeException("response status is ${response.errorCode}  msg is ${response.errorMsg}")))
        }
    }

}