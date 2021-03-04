package com.zj.play.compose.common.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.zj.play.compose.common.PlayAppBar
import dev.chrisbanes.accompanist.insets.statusBarsHeight

@Composable
fun ArticlePage(
    url: String,
    press: () -> Unit
) {
    val x5WebView = rememberX5WebViewWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.statusBarsHeight())
        PlayAppBar("网页", click = {
            if (x5WebView.canGoBack()) {
                //返回上个页面
                x5WebView.goBack()
                return@PlayAppBar
            } else {
                press.invoke()
            }
        })
        // Adds view to Compose
        AndroidView({ x5WebView }, modifier = Modifier.fillMaxSize()) { x5WebView ->
            // Reading zoom so that AndroidView recomposes when it changes. The getMapAsync lambda
            // is stored for later, Compose doesn't recognize state reads
            x5WebView.loadUrl(url)
        }
    }

}