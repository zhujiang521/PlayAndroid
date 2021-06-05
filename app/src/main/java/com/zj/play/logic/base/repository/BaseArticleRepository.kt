package com.zj.play.logic.base.repository

import android.accounts.NetworkErrorException
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zj.play.R
import com.zj.play.logic.model.*
import com.zj.play.logic.utils.NetworkUtils
import com.zj.play.logic.utils.showToast

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
        state.postValue(PlayLoading)
        if (!NetworkUtils.isConnected(application)) {
            showToast(application, R.string.no_network)
            state.postValue(PlayError(NetworkErrorException(application.getString(R.string.no_network))))
            return
        }
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