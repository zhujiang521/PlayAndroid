package com.zj.play.ui.page.system

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zj.play.logic.base.paging.SystemPagingSource
import com.zj.play.logic.base.repository.BaseArticlePagingRepository
import com.zj.play.logic.model.*
import com.zj.play.logic.network.PlayAndroidNetwork

class SystemRepository : BaseArticlePagingRepository() {

    override fun getPagingData(query: Query) = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        SystemPagingSource(query.cid)
    }.flow

    suspend fun getAndroidSystem(state: MutableLiveData<PlayState<List<AndroidSystemModel>>>) {
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