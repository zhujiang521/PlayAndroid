package com.zj.play.view.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import com.zj.core.view.BaseActivity
import com.zj.play.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private var isPortMode: Boolean = true

    override fun initView() {
        bindsPortScreen()
//        isPortMode = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
//        when (isPortMode) {
//            true -> bindsPortScreen()
//            false -> bindsLandScreen()
//        }

    }

    private fun bindsPortScreen() {
        homeView.init(supportFragmentManager)
    }

    private fun bindsLandScreen() {
        homeLandView.init(supportFragmentManager)
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        //super.onSaveInstanceState(outState)  // 解决fragment重影
    }

    override fun initData() {}

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onDestroy() {
        super.onDestroy()
        homeView.destroy()
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

}
