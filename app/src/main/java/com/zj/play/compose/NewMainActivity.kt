package com.zj.play.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.paging.ExperimentalPagingApi
import com.blankj.utilcode.util.BarUtils
import com.google.accompanist.insets.ProvideWindowInsets

class NewMainActivity : AppCompatActivity() {

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.transparentStatusBar(this)
        // This app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ProvideWindowInsets {
                NavGraphPage()
            }
        }

    }

}