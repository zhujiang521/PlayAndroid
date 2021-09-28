package com.zj.play.ui.page.search

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun SearchPage(back: () -> Unit, searchArticle: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchBar(back, searchArticle)
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

@Preview(name = "搜索头", widthDp = 360, heightDp = 70, showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar({}, {})
}
