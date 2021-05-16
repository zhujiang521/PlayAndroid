package com.zj.play

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.ExperimentalPagingApi
import com.zj.play.ui.main.NavGraph
import com.zj.play.ui.theme.PlayAndroidTheme
import com.zj.play.ui.view.ProvideImageLoader
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets


class MainActivity : ComponentActivity() {
    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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