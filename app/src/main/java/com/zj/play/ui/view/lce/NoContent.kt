package com.zj.play.ui.view.lce

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zj.play.R

@Composable
fun NoContent(tip: String = "当前无内容") {
    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.padding(vertical = 50.dp),
            painter = painterResource(id = R.drawable.no_content_image),
            contentDescription = "网络加载失败"
        )
        Text(text = tip, modifier = Modifier.padding(10.dp))
    }
}