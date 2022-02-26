package com.zj.play.ui.page.home

import android.accounts.NetworkErrorException
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zj.play.App
import com.zj.play.R
import com.zj.play.logic.base.paging.HomePagingSource
import com.zj.play.logic.base.repository.BaseArticlePagingRepository
import com.zj.play.logic.model.*
import com.zj.play.logic.network.PlayAndroidNetwork
import com.zj.play.logic.utils.NetworkUtils


/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/9/10
 * 描述：PlayAndroid
 *
 */

class HomeArticlePagingRepository : BaseArticlePagingRepository() {

    override fun getPagingData(query: Query) = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        HomePagingSource()
    }.flow

    /**
     * 获取banner
     */
    suspend fun getBanner(state: MutableLiveData<PlayState<List<BannerBean>>>, context: Context) {
        if (!NetworkUtils.isConnected(context)) {
            state.postValue(
                PlayError(NetworkErrorException(App.context?.getString(R.string.bad_network_view_tip)))
            )
            return
        }
        state.postValue(PlayLoading)
        val bannerResponse = PlayAndroidNetwork.getBanner()
        if (bannerResponse.errorCode == 0) {
            val bannerList = bannerResponse.data
            bannerList.forEach {
                it.data = it.imagePath
            }
            state.postValue(PlaySuccess(bannerList))
        } else {
            state.postValue(PlayError(RuntimeException("response status is ${bannerResponse.errorCode}  msg is ${bannerResponse.errorMsg}")))
        }
    }

}