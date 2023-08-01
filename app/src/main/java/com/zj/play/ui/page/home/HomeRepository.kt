package com.zj.play.ui.page.home

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zj.model.BannerBean
import com.zj.model.PlayError
import com.zj.model.PlayLoading
import com.zj.model.PlayState
import com.zj.model.PlaySuccess
import com.zj.model.Query
import com.zj.network.PlayAndroidNetwork
import com.zj.play.App
import com.zj.play.R
import com.zj.play.logic.paging.HomePagingSource
import com.zj.play.logic.repository.BaseArticlePagingRepository
import com.zj.utils.NetworkUtils.isConnected


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
    suspend fun getBanner(state: MutableState<PlayState<List<BannerBean>>>, context: Context) {
        if (!context.isConnected()) {
            state.value =
                PlayError(NetworkErrorException(App.context?.getString(R.string.bad_network_view_tip)))
            return
        }
        state.value = PlayLoading
        val bannerResponse = PlayAndroidNetwork.getBanner()
        if (bannerResponse.errorCode == 0) {
            val bannerList = bannerResponse.data
            bannerList.forEach {
                it.data = it.imagePath
            }
            state.value = PlaySuccess(bannerList)
        } else {
            state.value =
                PlayError(RuntimeException("response status is ${bannerResponse.errorCode}  msg is ${bannerResponse.errorMsg}"))
        }
    }

}