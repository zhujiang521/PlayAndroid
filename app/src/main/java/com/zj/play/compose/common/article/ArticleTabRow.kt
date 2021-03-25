package com.zj.play.compose.common.article

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zj.model.room.entity.ProjectClassify

@Composable
fun ArticleTabRow(
    position: Int?,
    data: List<ProjectClassify>,
    onTabClick: (Int, Int, Boolean) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = position ?: 0,
        modifier = Modifier.wrapContentWidth(),
        edgePadding = 3.dp
    ) {
        data.forEachIndexed { index, projectClassify ->
            Tab(
                text = { Text(projectClassify.name) },
                selected = position == index,
                onClick = {
                    onTabClick(index, projectClassify.id, false)
                }
            )
        }
        onTabClick(0, data[position ?: 0].id, true)
    }
}