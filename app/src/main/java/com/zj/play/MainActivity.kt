package com.zj.play

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.insets.ProvideWindowInsets
import com.zj.play.logic.utils.setAndroidNativeLightStatusBar
import com.zj.play.logic.utils.transparentStatusBar
import com.zj.play.ui.main.NavGraph
import com.zj.play.ui.theme.PlayAndroidTheme

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

}