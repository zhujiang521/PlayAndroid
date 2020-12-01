package com.zj.play.home.almanac

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.core.almanac.ScreenShotsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class AlmanacViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableLiveData<ShareState>()
    private var almanacRepository: AlmanacRepository = AlmanacRepository(application)

    val state: LiveData<ShareState>
        get() = _state

    private suspend fun addAlmanac(instance: Calendar, toString: String) {
        almanacRepository.addAlmanac(instance, toString)
    }

    fun shareAlmanac(activity: AlmanacActivity, view: View, calendar: Calendar) {
        _state.postValue(Sharing)
        viewModelScope.launch(Dispatchers.IO) {
            val almanacUri = almanacRepository.getAlmanacUri(calendar)
            if (almanacUri != null) {
                _state.postValue(ShareSuccess(almanacUri))
            } else {
                val tempUri = ScreenShotsUtils.takeScreenShot(activity, view)
                if (tempUri != null) {
                    addAlmanac(calendar, tempUri.toString())
                    _state.postValue(ShareSuccess(tempUri))
                } else {
                    _state.postValue(ShareError)
                }
            }
        }
    }

}

sealed class ShareState
object Sharing : ShareState()
data class ShareSuccess(val uri: Uri) : ShareState()
object ShareError : ShareState()