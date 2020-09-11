package com.zj.play.view.profile.history

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zj.play.room.entity.Article

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class BrowseHistoryViewModel(context: Context) : ViewModel() {

    private val pageLiveData = MutableLiveData<Int>()

    val articleList = ArrayList<Article>()

    val articleLiveData = Transformations.switchMap(pageLiveData) { page ->
        BrowseHistoryRepository(context).getBrowseHistory(page)
    }

    fun getArticleList(page: Int) {
        pageLiveData.value = page
    }

}