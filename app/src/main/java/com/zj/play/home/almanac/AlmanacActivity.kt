package com.zj.play.home.almanac

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.zj.core.almanac.IntentShareUtils
import com.zj.core.almanac.ProgrammerCalendar
import com.zj.core.almanac.ScreenShotsUtils
import com.zj.core.view.base.BaseActivity
import com.zj.play.R
import kotlinx.android.synthetic.main.activity_almanac.*

class AlmanacActivity : BaseActivity() {

    private val dayImages = intArrayOf(
        R.drawable.almanac_number_0, R.drawable.almanac_number_1, R.drawable.almanac_number_2,
        R.drawable.almanac_number_3, R.drawable.almanac_number_4, R.drawable.almanac_number_5,
        R.drawable.almanac_number_6, R.drawable.almanac_number_7, R.drawable.almanac_number_8,
        R.drawable.almanac_number_9
    )

    override fun getLayoutId(): Int = R.layout.activity_almanac


    override fun initView() {
        almanacTitleBar.setRightImage(R.drawable.almanac_share_button)
        almanacTitleBar.setRightImgOnClickListener {
            val tempUri: Uri? = ScreenShotsUtils.takeScreenShot(this, almanacRootView)
            IntentShareUtils.shareFile(this, tempUri, "黄历")
        }
    }

    override fun onResume() {
        super.onResume()
        bindView()
    }

    @SuppressLint("SetTextI18n")
    private fun bindView() {
        val hl = ProgrammerCalendar()
        val pickTodayLuck = hl.pickTodayLuck()
        almanacTvDate.text = hl.todayString
        almanacTvWeekDate.text = hl.weekString
        almanacTvSeat.text = "【座位朝向】面向" + hl.directions[hl.random(
            hl.iday,
            2
        ) % hl.directions.size] + "写程序，BUG 最少。"
        almanacTvDrink.text = "【今日宜饮】" + hl.pickRandomDrinks(2)
        almanacTvGoddess.text = "【女神亲近指数】" + hl.star(hl.random(hl.iday, 6) % 5 + 1)
        almanacTvYi.text = pickTodayLuck[0]
        almanacTvJi.text = pickTodayLuck[1]
        almanacIvNumberOne.setImageResource(dayImages[hl.todayInt / 10])
        almanacIvNumberTwo.setImageResource(dayImages[hl.todayInt % 10])
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, AlmanacActivity::class.java)
            context.startActivity(intent)
        }
    }

}