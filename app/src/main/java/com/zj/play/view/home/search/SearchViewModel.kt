package com.zj.play.view.home.search

import android.app.Application
import androidx.lifecycle.LiveData
import com.zj.core.view.BaseAndroidViewModel
import com.zj.play.room.entity.HotKey

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class SearchViewModel(application: Application) : BaseAndroidViewModel<List<HotKey>,HotKey,Boolean>(application) {

    override fun getData(page: Boolean): LiveData<Result<List<HotKey>>> {
        return SearchRepository(getApplication()).getHotKey()
    }

}