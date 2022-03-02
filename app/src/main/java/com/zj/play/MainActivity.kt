package com.zj.play

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.insets.ProvideWindowInsets
import com.zj.play.ui.main.NavGraph
import com.zj.play.ui.theme.GrayAppAdapter
import com.zj.play.ui.theme.PlayAndroidTheme
import com.zj.play.ui.theme.SKY_BLUE_THEME
import com.zj.utils.*
import dagger.hilt.android.AndroidEntryPoint

const val CHANGED_THEME_ACTION = "com.zj.play.CHANGED_THEME"
const val CHANGED_THEME = "default"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        setAndroidNativeLightStatusBar()
        setContent {
            val context = LocalContext.current

            var playTheme by remember { mutableStateOf(SKY_BLUE_THEME) }
            LaunchedEffect(Unit) {
                playTheme = DataStoreUtils.getSyncData(CHANGED_THEME, SKY_BLUE_THEME)
            }
            DisposableEffect(context) {
                val intentFilter = IntentFilter(CHANGED_THEME_ACTION)
                val broadcast = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        playTheme = intent.getIntExtra(CHANGED_THEME, SKY_BLUE_THEME)
                        XLog.e("theme:$playTheme")
                        DataStoreUtils.putSyncData(CHANGED_THEME, playTheme)
                    }
                }
                context.registerReceiver(broadcast, intentFilter)
                onDispose {
                    context.unregisterReceiver(broadcast)
                }
            }
            PlayAndroidTheme(playTheme) {
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