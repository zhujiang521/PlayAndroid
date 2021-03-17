package com.zj.play.compose.common.lce

import androidx.compose.runtime.Composable
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlayState
import com.zj.play.compose.model.PlaySuccess

@Composable
fun SetLcePage(playState: PlayState, onErrorClick: () -> Unit, content: @Composable () -> Unit) {

    when (playState) {
        PlayLoading -> {
            LoadingContent()
        }
        is PlayError -> {
            ErrorContent(onErrorClick = onErrorClick)
        }
        is PlaySuccess<*> -> {
            content()
        }
    }

}