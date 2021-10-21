package com.zj.play.ui.page.system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*
import com.zj.play.R
import com.zj.play.ui.view.PlayAppBar

@Composable
fun SystemPage() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.weather_rain))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val composition1 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.weather_stormshowersday))
    val progress1 by animateLottieCompositionAsState(
        composition = composition1,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayAppBar(title = "体系", showBack = false)
        LottieAnimation(composition = composition, progress = progress)
        LottieAnimation(composition = composition1, progress = progress1)
    }
}