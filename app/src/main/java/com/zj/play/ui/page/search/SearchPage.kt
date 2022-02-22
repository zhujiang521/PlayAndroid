package com.zj.play.ui.page.search

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.insets.statusBarsHeight
import com.zj.play.R
import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.model.HotkeyModel
import com.zj.play.logic.model.PlayState
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.view.lce.LcePage
import java.lang.Integer.max

@Composable
fun SearchPage(
    back: () -> Unit,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    hotkeyModels: PlayState<List<HotkeyModel>>,
    loadHotkey: () -> Unit,
    enterArticle: (ArticleModel) -> Unit,
    searchArticle: (String) -> Unit,
) {
    var loadArticleState by rememberSaveable { mutableStateOf(false) }
    if (!loadArticleState) {
        loadArticleState = true
        loadHotkey()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(back, searchArticle)
        if (lazyPagingItems.itemCount > 0) {
            ArticleListPaging(
                Modifier.fillMaxSize(),
                lazyPagingItems,
                enterArticle
            )
        } else {
            LcePage(
                playState = hotkeyModels,
                onErrorClick = loadHotkey
            ) {
                loadArticleState = true
                StaggeredGrid {
                    it.forEach {
                        Chip(text = it.name,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    searchArticle(it.name)
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(back: () -> Unit, searchArticle: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(Modifier.statusBarsHeight())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(43.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(
                modifier = Modifier
                    .wrapContentWidth(Alignment.Start), onClick = back
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "返回"
                )
            }
            Box(
                modifier = Modifier
                    .weight(5f)
                    .fillMaxWidth()
                    .padding(5.dp)
                    .border(
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = MaterialTheme.shapes.medium
                    )
                    .height(43.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = "请输入搜索内容",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(5.dp)
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = Icons.Rounded.Close, contentDescription = "",
                            modifier = Modifier
                                .clickable {
                                    value = ""
                                }
                                .fillMaxHeight()
                                .padding(end = 5.dp)
                        )
                    }
                }
                BasicTextField(
                    value = value,
                    onValueChange = { value = it },
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 45.dp)
                )
            }
            IconButton(
                modifier = Modifier.wrapContentWidth(Alignment.End),
                onClick = {
                    searchArticle(value)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "查询"
                )
            }
        }
    }
}


@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        // Keep track of the width of each row
        val rowWidths = IntArray(rows) { 0 }

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows) { 0 }

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.mapIndexed { index, measurable ->
            // Measure each child
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)

            placeable
        }

        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // Grid's height is the sum of the tallest element of each row
        // coerced to the height constraints
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // Y of each row, based on the height accumulation of previous rows
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }

        // Set the size of the parent layout
        layout(width, height) {
            // x co-ord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}


@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_head),
                contentDescription = "",
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Preview(name = "搜索头", widthDp = 360, heightDp = 70, showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar({}, {})
}
