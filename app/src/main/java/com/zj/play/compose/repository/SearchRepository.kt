package com.zj.play.compose.repository

import android.accounts.NetworkErrorException
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.Article
import com.zj.network.base.PlayAndroidNetwork
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlayState
import com.zj.play.compose.model.PlaySuccess

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
class SearchRepository(application: Application) {

    private val hotKeyDao = PlayDatabase.getDatabase(application).hotKeyDao()


    /**
     * 获取搜索热词
     */
    suspend fun getHotKey() {

        val hotKeyList = hotKeyDao.getHotKeyList()
        if (hotKeyList.isNotEmpty()) {
            Result.success(hotKeyList)
        } else {
            val projectTree = PlayAndroidNetwork.getHotKey()
            if (projectTree.errorCode == 0) {
                val hotKeyLists = projectTree.data
                hotKeyDao.insertList(hotKeyLists)
                Result.success(hotKeyLists)
            } else {
                Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
            }
        }
    }

    /**
     * 获取搜索结果
     */
    suspend fun getQueryArticleList(
        state: MutableLiveData<PlayState>,
        value: MutableLiveData<ArrayList<Article>>,
        page: Int,
        k: String
    ) {
        state.postValue(PlayLoading)
        val res: ArrayList<Article>
        val articleList = PlayAndroidNetwork.getQueryArticleList(page, k)
        if (page == 1) {
            res = arrayListOf()
            if (articleList.errorCode == 0) {
                res.addAll(articleList.data.datas)
                state.postValue(PlaySuccess(res))
                value.postValue(res)
            } else {
                state.postValue(PlayError(NetworkErrorException("")))
                value.postValue(res)
            }
        } else {
            res = value.value ?: arrayListOf()
            if (articleList.errorCode == 0) {
                res.addAll(articleList.data.datas)
                state.postValue(PlaySuccess(res))
                value.postValue(res)
            } else {
                state.postValue(PlayError(NetworkErrorException("")))
            }
        }

    }

}