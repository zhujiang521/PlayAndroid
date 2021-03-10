package com.zj.play.compose

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blankj.utilcode.util.BarUtils
import com.zj.core.util.LiveDataBus
import com.zj.play.compose.common.ProvideImageLoader
import com.zj.play.compose.theme.Play2Theme
import com.zj.play.compose.theme.PlayTheme
import com.zj.play.home.ArticleCollectBaseActivity
import com.zj.play.home.LOGIN_REFRESH
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

class NewMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.transparentStatusBar(this)
        // This app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {

            ProvideWindowInsets {

                ProvideImageLoader {
                    NavGraphPage()
                }
            }
        }

    }

}

const val THEME_REFRESH = "THEME_REFRESH"