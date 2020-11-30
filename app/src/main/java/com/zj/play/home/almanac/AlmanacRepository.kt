package com.zj.play.home.almanac

import android.app.Application
import android.net.Uri
import com.zj.core.util.CalendarUtils
import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.Almanac
import com.zj.network.repository.fire
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.*

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：11/30/20
 * 描述：PlayAndroid
 *
 */
class AlmanacRepository(application: Application) {

    private val almanacDao = PlayDatabase.getDatabase(application).almanacDao()

    fun getAlmanacUri(calendar: Calendar) = fire {
        coroutineScope {
            val julianDayFromCalendar =
                CalendarUtils.getJulianDayFromCalendar(calendar)
            val almanac = almanacDao.getAlmanac(julianDayFromCalendar)
            if (almanac?.imgUri != null) {
                Result.success(Uri.parse(almanac.imgUri))
            } else {
                Result.failure(RuntimeException("response status is "))
            }
        }
    }

    suspend fun addAlmanac(calendar: Calendar, imgUri: String) {
        almanacDao.insert(
            Almanac(
                julianDay = CalendarUtils.getJulianDayFromCalendar(calendar),
                imgUri = imgUri
            )
        )
    }

}