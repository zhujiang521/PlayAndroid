package com.zj.play.home.search

import android.app.Application
import com.zj.play.compose.viewmodel.BaseAndroidViewModel
import com.zj.model.room.entity.HotKey

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class SearchViewModel (application: Application) : BaseAndroidViewModel<List<HotKey>, HotKey, Boolean>(application) {

    private val searchRepository = SearchRepository(application)

    override suspend fun getData(page: Boolean) {
        searchRepository.getHotKey(_state)
    }

}