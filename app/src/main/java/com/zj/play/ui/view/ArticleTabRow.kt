package com.zj.play.ui.view

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.zj.play.logic.model.ClassifyModel
import com.zj.play.logic.utils.XLog
import com.zj.play.logic.utils.getHtmlText

@ExperimentalPagerApi
@Composable
fun ArticleTabRow(
    position: Int?,
    data: List<ClassifyModel>,
    pagerState: PagerState,
    onTabClick: (Int, Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = position ?: 0,
        modifier = Modifier.wrapContentWidth(),
        edgePadding = 3.dp,
        backgroundColor = MaterialTheme.colors.primary,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        data.forEachIndexed { index, projectClassify ->
            Tab(
                text = { Text(getHtmlText(projectClassify.name)) },
                selected = position == index,
                onClick = {
                    onTabClick(index, projectClassify.id)
                }
            )
        }
        onTabClick(0, data[position ?: 0].id)
    }
}

@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
fun ArticleTabRowPreview() {
    val data = arrayListOf<ClassifyModel>()
    for (index in 0..10) {
        data.add(
            ClassifyModel(index, index, index, "one$index", index, index, false, index)
        )
    }
    ArticleTabRow(0, data, rememberPagerState()) { index, id ->
        XLog.e("ArticleTabRowPreview", "ArticleTabRowPreview: $index  $id")
    }
}