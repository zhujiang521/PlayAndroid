package com.zj.play

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowInsetsCompat
import com.zj.play.ui.main.NavGraph
import com.zj.play.ui.theme.GrayAppAdapter
import com.zj.play.ui.theme.PlayAndroidTheme
import com.zj.play.ui.theme.themeTypeState
import com.zj.utils.*
import dagger.hilt.android.AndroidEntryPoint

const val CHANGED_THEME = "CHANGED_THEME"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayAndroidTheme(themeTypeState.value) {
                // 清明、七月十五或者是国家公祭日的时候展示黑白化效果
                GrayAppAdapter {
                    NavGraph()
                }
            }
        }

        applyImmersionWithWindowInsets()

        // 适配”三大金刚“导航栏  设置padding
        window.decorView.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)?.let {
            it.doOnApplyWindowInsets { _, windowInsets, _, _ ->
                if (!EdgeInsetDelegate.isGestureNavigation(windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()), this)) {
                    it.applyBottomInsetMargin()
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