package com.zj.play.view.official

import android.content.Context
import androidx.lifecycle.*
import com.zj.play.model.BannerBean
import com.zj.play.model.Article
import com.zj.play.network.Repository
import com.zj.play.room.PlayDatabase

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class OfficialViewModel(context: Context) : ViewModel() {

    val officialTreeLiveData =
        OfficialRepository(PlayDatabase.getDatabase(context).projectClassifyDao()).getProjectTree()
}