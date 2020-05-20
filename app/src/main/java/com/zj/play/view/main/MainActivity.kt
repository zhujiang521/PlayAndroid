package com.zj.play.view.main

import android.content.Context
import android.content.Intent
import com.zj.core.view.BaseActivity
import com.zj.play.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun initView() {
        homeView.init(supportFragmentManager)
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
