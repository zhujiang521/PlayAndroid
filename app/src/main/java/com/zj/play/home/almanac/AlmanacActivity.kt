package com.zj.play.home.almanac

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.zj.core.almanac.ProgrammerCalendar
import com.zj.core.util.IntentShareUtils
import com.zj.core.util.ProgressDialogUtil
import com.zj.core.util.showToast
import com.zj.core.view.base.BaseActivity
import com.zj.play.R
import kotlinx.android.synthetic.main.activity_almanac.*
import java.util.*

class AlmanacActivity : BaseActivity() {

    private val dayImages = intArrayOf(
        R.drawable.almanac_number_0, R.drawable.almanac_number_1, R.drawable.almanac_number_2,
        R.drawable.almanac_number_3, R.drawable.almanac_number_4, R.drawable.almanac_number_5,
        R.drawable.almanac_number_6, R.drawable.almanac_number_7, R.drawable.almanac_number_8,
        R.drawable.almanac_number_9
    )
    private var progressDialogUtil: ProgressDialogUtil? = null

    private val viewModel by lazy { ViewModelProvider(this).get(AlmanacViewModel::class.java) }

    override fun getLayoutId(): Int = R.layout.activity_almanac

    override fun initView() {
        almanacTitleBar.setRightImage(R.drawable.almanac_share_button)
        almanacTitleBar.setRightImgOnClickListener {
            viewModel.shareAlmanac(this, almanacRootView, Calendar.getInstance())
        }
        viewModel.state.observe(this) {
            when (it) {
                Sharing -> {
                    showProgressDialog()
                }
                is ShareSuccess -> {
                    hideProgressDialog()
                    IntentShareUtils.shareFile(this, it.uri, "黄历")
                }
                ShareError -> {
                    hideProgressDialog()
                    showToast("分享失败")
                }
            }
        }
    }

    private fun hideProgressDialog() {
        progressDialogUtil?.progressDialogDismiss()
    }

    private fun showProgressDialog() {
        progressDialogUtil = ProgressDialogUtil.getInstance(this)
        progressDialogUtil?.progressDialogShow("正在保存图片。。。")
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

    override fun onDestroy() {
        super.onDestroy()
        hideProgressDialog()
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, AlmanacActivity::class.java)
            context.startActivity(intent)
        }
    }

}