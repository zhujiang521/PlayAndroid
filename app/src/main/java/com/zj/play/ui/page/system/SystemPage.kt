package com.zj.play.ui.page.system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zj.play.R
import com.zj.play.ui.view.PlayAppBar

@Composable
fun SystemPage() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.weather_rain))
    val progress by animateLottieCompositionAsState(composition)
    Column(modifier = Modifier.fillMaxSize()) {
        PlayAppBar(title = "体系", showBack = false)
        LottieAnimation(composition = composition, progress = progress)
    }
}