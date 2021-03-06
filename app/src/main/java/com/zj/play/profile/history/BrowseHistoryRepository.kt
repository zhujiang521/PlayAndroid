package com.zj.play.profile.history

import android.accounts.NetworkErrorException
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.HISTORY
import com.zj.play.compose.common.PlayError
import com.zj.play.compose.common.PlayLoading
import com.zj.play.compose.common.PlayState
import com.zj.play.compose.common.PlaySuccess
import com.zj.play.main.login.composeFire
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
@ActivityRetainedScoped
class BrowseHistoryRepository @Inject constructor(val application: Application) {

    private val browseHistoryDao = PlayDatabase.getDatabase(application).browseHistoryDao()

    /**
     * 获取历史记录列表
     */
    fun getBrowseHistory(state:MutableLiveData<PlayState>, page: Int) = composeFire {
        state.postValue(PlayLoading)
        val projectClassifyLists = browseHistoryDao.getHistoryArticleList((page - 1) * 20,HISTORY)
        if (projectClassifyLists.isNotEmpty()) {
            state.postValue(PlaySuccess(projectClassifyLists))
        } else {
            state.postValue(PlayError(NetworkErrorException("")))
        }

    }

}