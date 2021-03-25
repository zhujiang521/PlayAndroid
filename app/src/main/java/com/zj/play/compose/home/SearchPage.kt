package com.zj.play.compose.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import com.zj.core.util.showToast
import com.zj.play.R
import com.zj.play.compose.MainActions
import com.zj.play.compose.common.article.ArticleListPaging
import com.zj.play.compose.common.article.ToTopButton
import com.zj.play.compose.common.lce.NoContent
import com.zj.play.compose.common.signin.TextFieldState
import com.zj.play.compose.model.Query
import com.zj.play.compose.viewmodel.SearchViewModel
import dev.chrisbanes.accompanist.insets.statusBarsHeight

@ExperimentalPagingApi
@Composable
fun SearchPage(
    actions: MainActions,
    viewModel: SearchViewModel = viewModel()
) {
    val searchState: TextFieldState = remember { TextFieldState() }
    var clickSearchState by remember { mutableStateOf(false) }
    val lazyPagingItems = viewModel.articleResult
    val listState = rememberLazyListState()
    Scaffold(
        backgroundColor = colorResource(id = R.color.yellow),
        topBar = {
            SearchTitleBar(searchState, actions.upPress, {
                clickSearchState = true
                viewModel.searchArticle(Query(k = searchState.text))
            })
        },
        content = {
            if (searchState.text.isBlank() || !clickSearchState) {
                NoContent()
            } else {
                ArticleListPaging(
                    listState = listState,
                    flowPagingItems = lazyPagingItems,
                    enterArticle = actions.enterArticle
                )
                ToTopButton(listState)
            }
        }
    )
}

@Composable
fun SearchTitleBar(searchState: TextFieldState, upPress: () -> Unit, onSearch: () -> Unit) {
    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(Modifier.statusBarsHeight())
        Box(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
        ) {
            TopAppBar(elevation = 0.dp, modifier = Modifier.height(48.dp)) {
                IconButton(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start),
                    onClick = upPress
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "back"
                    )
                }
                BasicTextField(
                    value = searchState.text,
                    onValueChange = {
                        searchState.text = it
                    },
                    textStyle = MaterialTheme.typography.subtitle1.copy(
                        color = LocalContentColor.current
                    ),
                    maxLines = 1,
                    cursorBrush = SolidColor(LocalContentColor.current),
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                )
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        onSearch()
                        showToast("搜索:${searchState.text}")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = ""
                    )
                }

            }
            if (searchState.text.isEmpty()) {
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                ) {
                    SearchHint()
                }
            }
        }
    }
}

@Composable
private fun SearchHint() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = ""
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "请输入关键字",
        )
    }
}