package com.zj.play.home.almanac

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
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

    private val calendarLiveData = MutableLiveData<Calendar>()

    val almanacUriLiveData = Transformations.switchMap(calendarLiveData) { calendar ->
        AlmanacRepository(application).getAlmanacUri(calendar)
    }

    fun getAlmanacUri(calendar: Calendar) {
        calendarLiveData.value = calendar
    }

    fun addAlmanac(instance: Calendar, toString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            AlmanacRepository(getApplication()).addAlmanac(instance,toString)
        }
    }

}