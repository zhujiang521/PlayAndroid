package com.zj.core.util

import java.util.*

object CalendarUtils {

    private const val BASE_JU_LIAN_DAY = 2415751
    private const val MONDAY_BEFORE_JULIAN_BASE = 2415749

    fun getJulianDayFromCalendar(mCalendar: Calendar): Int {
        return getJulianDay(
            mCalendar[Calendar.YEAR], mCalendar[Calendar.MONTH] + 1,
            mCalendar[Calendar.DAY_OF_MONTH]
        )
    }

    private fun getJulianDay(year: Int, month: Int, day: Int): Int {
        val a = (14 - month) / 12
        val y = year + 4800 - a
        val m = month + 12 * a - 3
        return day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045
    }

    fun getCalendarFromJulianDay(julianday: Int): Calendar {
        val tzz = TimeZone.getDefault()
        if (julianday < MONDAY_BEFORE_JULIAN_BASE) {
            return Calendar.getInstance(tzz)
        }
        val a = julianday + 32044
        val b = (4 * a + 3) / 146097
        val c = a - 146097 * b / 4
        val d = (4 * c + 3) / 1461
        val e = c - 1461 * d / 4
        val m = (5 * e + 2) / 153
        val day = e - (153 * m + 2) / 5 + 1
        val month = m + 3 - 12 * (m / 10)
        val year = 100 * b + d - 4800 + m / 10
        val mCalendar = Calendar.getInstance(tzz)
        mCalendar[year, month - 1] = day
        return mCalendar
    }

    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }

    fun getWeeksSinceBaseFromJulianDay(julianDay: Int, firstDayOfWeek: Int): Int {
        var diff = GregorianCalendar.WEDNESDAY - firstDayOfWeek
        if (diff < 0) {
            diff += 7
        }
        val refDay = BASE_JU_LIAN_DAY - diff //2415748
        return (julianDay - refDay) / 7 //41976/7
    }

    fun getJulianMondayFromWeeksSinceBase(week: Int): Int {
        return MONDAY_BEFORE_JULIAN_BASE + week * 7
    }

    fun getNumberOfWeeks(tmp: Calendar, weekStart: Int): Int {
        val days = tmp.getActualMaximum(Calendar.DAY_OF_MONTH)
        var diff = tmp[Calendar.DAY_OF_WEEK] - 1 - weekStart
        if (diff < 0) {
            diff += 7
        }
        var weekCount = (days + diff) / 7
        val reminder = (days + diff) % 7
        if (reminder > 0) {
            weekCount += 1
        }
        return weekCount
    }

    fun setCalendarFromPosition(mCalendar: Calendar, position: Int) {
        val year = position / 12
        val month = position % 12
        mCalendar[1902 + year, month] = 1
    }

    /**
     * 返回月份对应的季度数
     * @param month
     * @return 1-4代表第1季度 - 第4季度
     */
    fun getQuarterByMonth(month: Int): Int {
        return when (month) {
            in 0..2 // 1-3月;0,1,2
            -> 1
            in 3..5 // 4-6月;3,4,5
            -> 2
            in 6..8 // 7-9月;6,7,8
            -> 3
            else  // 10-12月;10,11,12
            -> 4
        }
    }
}