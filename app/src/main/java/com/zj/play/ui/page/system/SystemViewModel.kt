package com.zj.play.ui.page.system

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.model.AndroidSystemModel
import com.zj.model.PlayState
import com.zj.model.PlaySuccess
import com.zj.model.Query
import com.zj.play.logic.repository.BaseArticlePagingRepository
import com.zj.play.logic.viewmodel.BaseArticleViewModel
import com.zj.utils.XLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/17
 * 描述：PlayAndroid
 *
 */

class SystemViewModel(application: Application) : BaseArticleViewModel(application) {

    private var job: Job? = null

    private val mutableLiveData = MutableLiveData<PlayState<List<AndroidSystemModel>>>()

    val androidSystemState: LiveData<PlayState<List<AndroidSystemModel>>>
        get() = mutableLiveData

    fun getAndroidSystem() {
        if (androidSystemState.value is PlaySuccess<*>) {
            XLog.e("Do not request existing data")
            return
        }
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            SystemRepository().getAndroidSystem(mutableLiveData, getApplication())
        }
    }

    override val repositoryArticle: BaseArticlePagingRepository
        get() = SystemRepository()

    fun getSystemArticle(cid: Int) {
        searchArticle(Query(cid = cid))
    }

    private val _systemState = MutableLiveData(AndroidSystemModel(arrayListOf()))
    val systemState: LiveData<AndroidSystemModel> = _systemState

    fun onSystemModelChanged(systemModel: AndroidSystemModel) {
        _systemState.value = systemModel
    }

}