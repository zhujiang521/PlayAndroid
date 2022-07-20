package com.zj.play.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import com.zj.core.util.showToast
import com.zj.core.view.base.BaseActivity
import com.zj.play.R
import com.zj.play.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    var isPort = true

    override fun initView() {
        isPort = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        when (isPort) {
            true -> binding.homeView?.init(supportFragmentManager, viewModel)
            false -> binding.homeLandView?.init(supportFragmentManager, viewModel)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        //super.onSaveInstanceState(outState)  // 解决fragment重影
    }

    override fun getLayoutView(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    private var exitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            showToast(getString(R.string.exit_program))
            exitTime = System.currentTimeMillis()
        } else {
            exitProcess(0)
        }
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

}
