package com.zj.play.compose.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.zj.play.R
import dev.chrisbanes.accompanist.insets.statusBarsHeight

@Composable
fun PlayAppBar(title: String, showBack: Boolean = true, click: (() -> Unit)? = null) {
    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(Modifier.statusBarsHeight())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(43.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showBack) {
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.Start), onClick = click!!
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "back"
                    )
                }
            } else {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.End),
                )
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                text = title,
                style = MaterialTheme.typography.subtitle1,
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.End),
            )
        }
    }
}