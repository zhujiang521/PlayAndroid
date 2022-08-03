package com.zj.core.util

import android.content.Context
import android.icu.util.Calendar
import android.text.format.DateUtils

object DateUtils {

    fun getDateString(context: Context?, timeMills: Long): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        calendar.timeInMillis = timeMills
        val flag = if (year == calendar.get(Calendar.YEAR)) {
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME
        } else {
            DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME
        }
        return DateUtils.formatDateTime(context, timeMills, flag)
    }

}