package com.zj.play

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.insets.ProvideWindowInsets
import com.zj.play.ui.main.NavGraph
import com.zj.play.ui.theme.PlayAndroidTheme
import com.zj.utils.cancelToast
import com.zj.utils.setAndroidNativeLightStatusBar
import com.zj.utils.transparentStatusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        setAndroidNativeLightStatusBar()
        setContent {
            PlayAndroidTheme {
                ProvideWindowInsets {
                    NavGraph()
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