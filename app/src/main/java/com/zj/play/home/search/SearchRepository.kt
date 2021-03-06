package com.zj.play.home.search

import android.accounts.NetworkErrorException
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zj.model.room.PlayDatabase
import com.zj.network.base.PlayAndroidNetwork
import com.zj.play.compose.common.PlayError
import com.zj.play.compose.common.PlayLoading
import com.zj.play.compose.common.PlayState
import com.zj.play.compose.common.PlaySuccess
import com.zj.play.main.login.composeFires
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
class SearchRepository @Inject constructor(application: Application) {

    private val hotKeyDao = PlayDatabase.getDatabase(application).hotKeyDao()

    /**
     * 获取搜索热词
     */
    suspend fun getHotKey(state: MutableLiveData<PlayState>) {
        state.postValue(PlayLoading)
        val hotKeyList = hotKeyDao.getHotKeyList()
        if (hotKeyList.isNotEmpty()) {
            state.postValue(PlaySuccess(hotKeyList))
        } else {
            val projectTree = PlayAndroidNetwork.getHotKey()
            if (projectTree.errorCode == 0) {
                val hotKeyLists = projectTree.data
                hotKeyDao.insertList(hotKeyLists)
                state.postValue(PlaySuccess(hotKeyLists))
            } else {
                state.postValue(PlayError(NetworkErrorException("")))
            }
        }
    }

    /**
     * 获取搜索结果
     */
    fun getQueryArticleList(page: Int, k: String) = composeFires {
        PlayAndroidNetwork.getQueryArticleList(page, k)
    }

}