package com.zj.play.home.almanac

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.zj.core.almanac.ProgrammerCalendar
import com.zj.core.view.base.BaseActivity
import com.zj.play.R
import kotlinx.android.synthetic.main.activity_almanac.*

class AlmanacActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_almanac

    override fun initView() {
        var text = ""
        val hl = ProgrammerCalendar()
        text += "今天是：" + hl.todayString + "\n"
        text += "座位朝向：面向" + hl.directions[hl.random(
            hl.iday,
            2
        ) % hl.directions.size] + "写程序，BUG 最少。" + "\n"
        text += "今日宜饮：" + hl.pickRandomDrinks(2) + "\n"
        text += "女神亲近指数：" + hl.star(hl.random(hl.iday, 6) % 5 + 1) + "\n"
        val pickTodayLuck = hl.pickTodayLuck()
        text += "宜：" + pickTodayLuck[0] + "\n"
        text += "忌：" + pickTodayLuck[1] + "\n"
        almanacText.text = text
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, AlmanacActivity::class.java)
            context.startActivity(intent)
        }
    }

}