package com.zj.play.logic.repository

import android.accounts.NetworkErrorException
import android.app.Application
import androidx.compose.runtime.MutableState
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
    suspend fun getTree(state: MutableState<PlayState<List<ClassifyModel>>>) {
        if (!application.isConnected()) {
            state.value =
                PlayError(NetworkErrorException(application.getString(R.string.no_network)))
            return
        }
        state.value = PlayLoading
        val tree = getArticleTree()
        if (tree.errorCode == 0) {
            val projectList = tree.data
            state.value = PlaySuccess(projectList)
        } else {
            state.value =
                PlayError(NetworkErrorException(application.getString(R.string.no_network)))
        }
    }

    abstract suspend fun getArticleTree(): BaseModel<List<ClassifyModel>>

}