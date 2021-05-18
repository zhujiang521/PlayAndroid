package com.zj.play.ui.view.lce

import android.widget.ProgressBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zj.play.R

@Composable
fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Adds view to Compose
        AndroidView(
            { ProgressBar(it) }, modifier = Modifier
                .width(200.dp)
                .height(110.dp)
        ) {
            it.indeterminateDrawable =
                AppCompatResources.getDrawable(it.context, R.drawable.loading_animation)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoadingContentPreview(){
    LoadingContent()
}