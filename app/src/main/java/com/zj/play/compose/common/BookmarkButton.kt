package com.zj.play.compose.common

import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import com.zj.play.R

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clickLabel = if (isBookmarked) "取消收藏" else "收藏"
    IconToggleButton(
        checked = isBookmarked,
        onCheckedChange = { onClick() },
        modifier = modifier.semantics {
            // Use a custom click label that accessibility services can communicate to the user.
            // We only want to override the label, not the actual action, so for the action we pass null.
            this.onClick(label = clickLabel, action = null)
        }
    ) {
        Icon(
            if (isBookmarked) painterResource(id = R.drawable.ic_baseline_bookmark_24) else painterResource(
                id = R.drawable.ic_baseline_bookmark_border_24
            ),
            contentDescription = null // handled by click label of parent
        )
    }
}