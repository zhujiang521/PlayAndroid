package com.zj.play.ui.page.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zj.play.logic.base.paging.SearchPagingSource
import com.zj.play.logic.base.repository.BaseArticlePagingRepository
import com.zj.play.logic.model.*
import com.zj.play.logic.network.PlayAndroidNetwork

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/9/10
 * 描述：PlayAndroid
 *
 */

class SearchRepository : BaseArticlePagingRepository() {

    override fun getPagingData(query: Query) = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        SearchPagingSource(query.k)
    }.flow

    /**
     * 获取banner
     */
    suspend fun getHotKey(state: MutableLiveData<PlayState<List<HotkeyModel>>>) {
        state.postValue(PlayLoading)
        val response = PlayAndroidNetwork.getHotkeyModel()
        if (response.errorCode == 0) {
            val bannerList = response.data
            state.postValue(PlaySuccess(bannerList))
        } else {
            state.postValue(PlayError(RuntimeException("response status is ${response.errorCode}  msg is ${response.errorMsg}")))
        }
    }

}