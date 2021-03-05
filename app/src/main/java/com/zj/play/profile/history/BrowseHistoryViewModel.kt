package com.zj.play.profile.history

import android.app.Application
import com.zj.play.compose.viewmodel.BaseAndroidViewModel
import com.zj.model.room.entity.Article

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class BrowseHistoryViewModel constructor(application: Application) : BaseAndroidViewModel<List<Article>, Article, Int>(application) {

    private val browseHistoryRepository = BrowseHistoryRepository(application)

    override suspend fun getData(page: Int) {
        browseHistoryRepository.getBrowseHistory(_state,page)
    }

}