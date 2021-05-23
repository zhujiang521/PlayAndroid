package com.zj.play

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlayAndroidTheme {
        Greeting("Android")
    }
}