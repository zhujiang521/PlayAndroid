package com.zj.play.compose.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@Composable
fun PlayAppBar(title: String, showBack: Boolean = true, click: (() -> Unit)? = null) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(50.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        if (showBack) {
//            Image(
//                modifier = Modifier
//                    .size(30.dp)
//                    .padding(10.dp)
//                    .clickable { click?.invoke() },
//                painter = painterResource(id = R.drawable.img_back_nomal),
//                contentDescription = ""
//            )
//        }
//        Text(
//            text = title,
//            style = MaterialTheme.typography.subtitle1,
//            modifier = Modifier.clickable {
//                click!!
//            }
//        )
//    }

    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        contentColor = Color.White, // always white as image has dark scrim
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {
        if (showBack) {
            IconButton(onClick = click!!) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "back"
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1,
        )
        Spacer(modifier = Modifier.weight(1f))
    }

}