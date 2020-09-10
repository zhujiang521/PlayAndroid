package com.zj.play.view.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.zj.core.view.BaseActivity
import com.zj.play.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    var isPort = true

    override fun initView() {
        isPort = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        when (isPort) {
            true -> homeView.init(supportFragmentManager, viewModel)
            false -> homeLandView.init(supportFragmentManager, viewModel)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        //super.onSaveInstanceState(outState)  // 解决fragment重影
    }

    override fun initData() {}

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

}
