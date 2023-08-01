package com.zj.play.ui.page.search

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.zj.model.HotkeyModel
import com.zj.model.PlayLoading
import com.zj.model.PlayState
import com.zj.model.Query
import com.zj.network.PlayAndroidNetwork
import com.zj.play.logic.repository.BaseArticlePagingRepository
import com.zj.play.logic.viewmodel.BaseArticleViewModel
import kotlinx.coroutines.Job

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/17
 * 描述：PlayAndroid
 *
 */
class SearchViewModel(application: Application) : BaseArticleViewModel(application) {

    private var hotkeyJob: Job? = null

    override val repositoryArticle: BaseArticlePagingRepository
        get() = SearchRepository()

    fun getSearchArticle(keyword: String) {
        searchArticle(Query(k = keyword))
    }

    private val _hotkeyState = mutableStateOf<PlayState<List<HotkeyModel>>>(PlayLoading)

    val hotkeyState: State<PlayState<List<HotkeyModel>>>
        get() = _hotkeyState

    fun getHotkeyList() {
        hotkeyJob?.cancel()
        hotkeyJob = (repositoryArticle as SearchRepository)
            .http(
                scope = viewModelScope,
                request = { PlayAndroidNetwork.getHotkeyModel() },
                state = _hotkeyState
            )
    }

}
