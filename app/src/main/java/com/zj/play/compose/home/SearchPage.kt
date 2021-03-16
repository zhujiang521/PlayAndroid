package com.zj.play.compose.home

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.zj.play.R
import com.zj.play.compose.MainActions
import com.zj.play.compose.common.PlayAppBar

@Composable
fun SearchPage(
    onNavigationEvent: MainActions,
) {
    Scaffold(
        topBar = {
            PlayAppBar(
                title = stringResource(id = R.string.search),
                click = onNavigationEvent.upPress
            )
        },
        content = {

        }
    )
}