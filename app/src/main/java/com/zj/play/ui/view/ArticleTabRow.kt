package com.zj.play.ui.view

import android.util.Log
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zj.play.logic.model.ClassifyModel
import com.zj.play.logic.utils.getHtmlText

@Composable
fun ArticleTabRow(
    position: Int?,
    data: List<ClassifyModel>,
    onTabClick: (Int, Int, Boolean) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = position ?: 0,
        modifier = Modifier.wrapContentWidth(),
        edgePadding = 3.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        data.forEachIndexed { index, projectClassify ->
            Tab(
                text = { Text(getHtmlText(projectClassify.name)) },
                selected = position == index,
                onClick = {
                    onTabClick(index, projectClassify.id, false)
                }
            )
        }
        onTabClick(0, data[position ?: 0].id, true)
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleTabRowPreview() {
    val data = arrayListOf<ClassifyModel>()
    for (index in 0..10) {
        data.add(
            ClassifyModel(index, index, index, "one$index", index, index, false, index)
        )
    }
    ArticleTabRow(0, data) { index, id, load ->
        Log.e("ArticleTabRowPreview", "ArticleTabRowPreview: $index  $id    $load")
    }
}