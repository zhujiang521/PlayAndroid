package com.zj.play

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.zj.play.ui.main.NavGraph
import com.zj.play.ui.theme.GrayAppAdapter
import com.zj.play.ui.theme.PlayAndroidTheme
import com.zj.play.ui.theme.themeTypeState
import com.zj.utils.cancelToast
import com.zj.utils.setAndroidNativeLightStatusBar
import com.zj.utils.transparentStatusBar
import dagger.hilt.android.AndroidEntryPoint

const val CHANGED_THEME = "CHANGED_THEME"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        setAndroidNativeLightStatusBar()
        setContent {
            PlayAndroidTheme(themeTypeState.value) {
                ProvideWindowInsets {
                    // 清明、七月十五或者是国家公祭日的时候展示黑白化效果
                    GrayAppAdapter {
                        NavGraph()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 当 Activity 结束时表明整个项目已经结束，将 Toast 取消显示。
        cancelToast()
    }

}