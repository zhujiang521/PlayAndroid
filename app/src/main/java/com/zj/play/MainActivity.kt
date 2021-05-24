package com.zj.play

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.paging.ExperimentalPagingApi
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.zj.play.logic.utils.BarUtils
import com.zj.play.ui.main.NavGraph
import com.zj.play.ui.theme.PlayAndroidTheme
import com.zj.play.ui.view.ProvideImageLoader

class MainActivity : ComponentActivity() {
    @ExperimentalPagerApi
    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.transparentStatusBar(this)
        setAndroidNativeLightStatusBar()
        setContent {
            PlayAndroidTheme {
                ProvideWindowInsets {
                    ProvideImageLoader {
                        NavGraph()
                    }
                }
            }
        }
    }

    /**
     * 状态栏反色
     */
    private fun setAndroidNativeLightStatusBar() {
        val decor = window.decorView
        val isDark = resources.configuration.uiMode == 0x21
        if (!isDark) {
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

}