package com.zj.play.profile.history

import android.app.Application
import androidx.lifecycle.LiveData
import com.zj.core.view.base.BaseAndroidViewModel
import com.zj.model.room.entity.Article

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class BrowseHistoryViewModel(application: Application) : BaseAndroidViewModel<List<Article>, Article, Int>(application) {

    override fun getData(page: Int): LiveData<Result<List<Article>>> {
        return BrowseHistoryRepository(getApplication()).getBrowseHistory(page)
    }

}