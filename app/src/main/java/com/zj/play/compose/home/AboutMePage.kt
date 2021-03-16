package com.zj.play.compose.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zj.play.R
import com.zj.play.compose.MainActions
import com.zj.play.compose.common.PlayAppBar

@Composable
fun AboutMePage(
    onNavigationEvent: MainActions,
) {
    Scaffold(
        topBar = {
            PlayAppBar(
                title = stringResource(id = R.string.about_me),
                click = onNavigationEvent.upPress
            )
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .padding(10.dp),
                    painter = painterResource(id = R.drawable.img_head),
                    contentDescription = ""
                )
                Text(modifier = Modifier.padding(10.dp), text = "ZhuJiang")
                Text(modifier = Modifier.padding(10.dp), text = "只要开始，永远不晚；只要努力，就有可能")
                Text(modifier = Modifier.padding(30.dp), text = "下面是我的公众号，赶快扫码关注吧！！！")
                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .shadow(
                            2.dp, shape = RoundedCornerShape(
                                5.dp
                            )
                        ),
                    painter = painterResource(id = R.drawable.we_chart_mark),
                    contentDescription = ""
                )
            }
        }
    )
}