package com.zj.play.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.zj.play.compose.utils.LocalBackDispatcher
import com.zj.play.compose.utils.ProvideImageLoader
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

class NewMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ProvideWindowInsets {
                ProvideImageLoader {
                    NavGraph()
                }
            }
        }
    }

}