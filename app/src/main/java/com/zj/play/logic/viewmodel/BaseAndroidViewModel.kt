package com.zj.play.logic.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.zj.model.ClassifyModel
import com.zj.model.PlayLoading
import com.zj.model.PlayState
import com.zj.model.PlaySuccess
import com.zj.utils.XLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/05/18
 * 描述：PlayAndroid
 *
 */

abstract class BaseAndroidViewModel(application: Application) : BaseArticleViewModel(application) {

    protected val mutableTreeState = mutableStateOf<PlayState<List<ClassifyModel>>>(PlayLoading)

    val treeState: State<PlayState<List<ClassifyModel>>>
        get() = mutableTreeState

    abstract suspend fun getData()

    fun getDataList() {
        if (mutableTreeState.value is PlaySuccess<*>) {
            XLog.e("Do not request existing data")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            getData()
        }
    }

}