package com.zj.play.ui.view.lce

import androidx.compose.runtime.Composable
import com.zj.play.logic.model.PlayError
import com.zj.play.logic.model.PlayLoading
import com.zj.play.logic.model.PlayState
import com.zj.play.logic.model.PlaySuccess

@Composable
fun LcePage(playState: PlayState, onErrorClick: () -> Unit, content: @Composable () -> Unit) {

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