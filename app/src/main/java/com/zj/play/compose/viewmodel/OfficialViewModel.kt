package com.zj.play.compose.viewmodel

import android.app.Application
import com.zj.play.compose.repository.OfficialRepository

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class OfficialViewModel(application: Application) :
    BaseAndroidViewModel<Boolean>(application) {

    private val officialRepository = OfficialRepository(application)

    override suspend fun getData(page: Boolean) {
        return officialRepository.getTree(mutableLiveData, page)
    }
}