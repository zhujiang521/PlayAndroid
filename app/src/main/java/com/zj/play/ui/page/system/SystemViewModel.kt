package com.zj.play.ui.page.system

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.play.logic.base.repository.BaseArticlePagingRepository
import com.zj.play.logic.base.viewmodel.BaseArticleViewModel
import com.zj.play.logic.model.AndroidSystemModel
import com.zj.play.logic.model.PlayState
import com.zj.play.logic.model.Query
import com.zj.play.ui.page.search.SearchRepository
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
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            SystemRepository().getAndroidSystem(mutableLiveData)
        }
    }

    override val repositoryArticle: BaseArticlePagingRepository
        get() = SystemRepository()

    fun getSystemArticle(cid: Int) {
        searchArticle(Query(cid = cid))
    }

}