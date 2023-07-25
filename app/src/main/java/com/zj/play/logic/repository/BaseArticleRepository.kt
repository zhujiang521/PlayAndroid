package com.zj.play.logic.repository

import android.accounts.NetworkErrorException
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zj.model.BaseModel
import com.zj.model.ClassifyModel
import com.zj.model.PlayError
import com.zj.model.PlayLoading
import com.zj.model.PlayState
import com.zj.model.PlaySuccess
import com.zj.play.R
import com.zj.utils.NetworkUtils.isConnected

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/5/16
 * 描述：PlayAndroid
 *
 */
abstract class BaseArticleRepository(private val application: Application) :
    BaseArticlePagingRepository() {

    /**
     * 获取标题列表
     */
    suspend fun getTree(state: MutableLiveData<PlayState<List<ClassifyModel>>>) {
        if (!application.isConnected()) {
            state.postValue(PlayError(NetworkErrorException(application.getString(R.string.no_network))))
            return
        }
        state.postValue(PlayLoading)
        val tree = getArticleTree()
        if (tree.errorCode == 0) {
            val projectList = tree.data
            state.postValue(PlaySuccess(projectList))
        } else {
            state.postValue(PlayError(NetworkErrorException(application.getString(R.string.no_network))))
        }
    }

    abstract suspend fun getArticleTree(): BaseModel<List<ClassifyModel>>

}