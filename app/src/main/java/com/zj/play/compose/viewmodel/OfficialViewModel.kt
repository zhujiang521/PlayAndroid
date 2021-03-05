package com.zj.play.compose.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zj.model.room.entity.ProjectClassify
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
    BaseAndroidViewModel<List<ProjectClassify>, Unit, Boolean>(application) {

    private val officialRepository = OfficialRepository(application)

    private val _position = MutableLiveData(0)
    val position: LiveData<Int> = _position

    fun onPositionChanged(position: Int) {
        _position.value = position
    }

    override suspend fun getData(page: Boolean) {
        Log.e("ZHUJIANG123", "getData: 111" )
        return officialRepository.getWxArticleTree(_state, page)
    }
}