package com.zj.core.almanac

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：11/25/20
 * 描述：PlayAndroid
 *
 */
@SuppressLint("SimpleDateFormat")
class ProgrammerCalendar {
    var iday = 0
    private val weeks = arrayOf("日", "一", "二", "三", "四", "五", "六")
    val directions = arrayOf("北方", "东北方", "东方", "东南方", "南方", "西南方", "西方", "西北方")
    private val tools = arrayOf(
        "Eclipse写程序",
        "MSOffice写文档",
        "记事本写程序",
        "Windows10",
        "Linux",
        "MacOS",
        "IE",
        "Android设备",
        "iOS设备"
    )

    private val drinks = arrayOf(
        "水",
        "茶",
        "红茶",
        "绿茶",
        "咖啡",
        "奶茶",
        "可乐",
        "鲜奶",
        "豆奶",
        "果汁",
        "果味汽水",
        "苏打水",
        "运动饮料",
        "酸奶",
        "酒"
    )

    /*
	 * 注意：本程序中的“随机”都是伪随机概念，以当前的天为种子。
	 */
    fun random(daySeed: Int, indexSeed: Int): Int {
        var n = daySeed % 11117
        for (i in 0 until 100 + indexSeed) {
            n *= n
            n %= 11117 //11117是个质数
        }
        return n
    }

    val todayString: String
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            return "${calendar[Calendar.YEAR]}年${(calendar[Calendar.MONTH] + 1)}月${calendar[Calendar.DAY_OF_MONTH]}日 "
        }

    val todayInt: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            return calendar[Calendar.DAY_OF_MONTH]
        }

    val weekString: String
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            return "星期${weeks[calendar[Calendar.DAY_OF_WEEK] - 1]}"
        }

    fun star(num: Int): String {
        var result = ""
        var i = 0
        while (i < num) {
            result += "★"
            i++
        }
        while (i < 5) {
            result += "☆"
            i++
        }
        return result
    }

    private val isWeekend: Boolean
        get() {
            Locale.setDefault(Locale.CHINA)
            val calendar = Calendar.getInstance()
            return calendar[Calendar.DAY_OF_WEEK] == 1 || calendar[Calendar.DAY_OF_WEEK] == 7
        }

    private fun filter(): List<ActivitiesEnum> {
        val thisEnum: MutableList<ActivitiesEnum> = ArrayList()

        // 周末的话，只留下 weekend = true 的事件
        if (isWeekend) {
            for (e in ActivitiesEnum.values()) {
                if (e.weekend!!) {
                    thisEnum.add(e)
                }
            }
            return thisEnum
        }
        return ArrayList(listOf(*ActivitiesEnum.values()))
    }

    fun pickTodayLuck(): Array<String> {
        val activities = filter()
        val numGood = random(iday, 98) % 3 + 1
        val numBad = random(iday, 87) % 3 + 1
        val eventArr = pickRandomActivity(activities, numGood + numBad)
        var should = ""
        for (i in 0 until numGood) {
            should +=
                "    " + eventArr[i]["name"] + if ((eventArr[i]["good"]
                        ?: error("")).isNotBlank()
                ) "：" + eventArr[i]["good"] else ""
            should += "\n"
        }
        var notSuitable = ""
        for (i in 0 until numBad) {
            notSuitable +=
                "    " + eventArr[numGood + i]["name"] + if ((eventArr[numGood + i]["bad"] ?: error(
                        ""
                    )).isNotBlank()
                ) "：" + eventArr[numGood + i]["bad"] else ""
            notSuitable += "\n"
        }
        return arrayOf(
            should.substring(0, should.length - 1),
            notSuitable.substring(0, notSuitable.length - 1)
        )
    }

    /**
     * 从数组中随机挑选 size 个
     * @param size
     * @return
     */
    private fun pickRandom(_activities: List<ActivitiesEnum>, size: Int): List<ActivitiesEnum> {
        val result: MutableList<ActivitiesEnum> = ArrayList()
        for (ae in _activities) {
            result.add(ae)
        }
        for (i in 0 until _activities.size - size) {
            val index = random(iday, i) % result.size
            result.removeAt(index)
        }
        return result
    }

    /**
     * 从数组中随机挑选 size 个
     * @param size
     * @return
     */
    fun pickRandomDrinks(size: Int): List<String> {
        val result = drinks.toMutableList()
        for (i in 0 until drinks.size - size) {
            val index = random(iday, i) % result.size
            result.removeAt(index)
        }
        return result


    }

    //  枚举 中随机挑选 size 个
    private fun pickRandomActivity(
        _activities: List<ActivitiesEnum>,
        size: Int
    ): List<Map<String, String?>> {
        val pickedEvents = pickRandom(_activities, size)
        val mapList: MutableList<Map<String, String?>> = ArrayList()
        for (i in pickedEvents.indices) {
            mapList.add(parse(pickedEvents[i]))
        }
        return mapList
    }

    /**
     * 解析占位符并替换成随机内容
     * @param ae
     * @return
     */
    private fun parse(ae: ActivitiesEnum): Map<String, String?> {
        val map: MutableMap<String, String?> = HashMap()
        map["name"] = ae.names
        map["good"] = ae.good
        map["bad"] = ae.bad
        if (map["name"]!!.indexOf("%t") != -1) {
            map["name"] = map["name"]!!.replace(
                "%t".toRegex(),
                tools[random(iday, 11) % tools.size]
            )
        }
        if (map["name"]!!.indexOf("%t") != -1) {
            map["name"] =
                map["name"]!!.replace("%l".toRegex(), (random(iday, 12) % 247 + 30).toString() + "")
        }
        return map
    }

    init {
        val sdf = SimpleDateFormat("yyyyMMdd")
        try {
            iday = sdf.format(Date()).toInt()
        } catch (e: Exception) {
            e.printStackTrace() //懒得引用log了
        }
    }
}