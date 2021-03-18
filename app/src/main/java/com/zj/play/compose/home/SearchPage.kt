package com.zj.play.compose.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.core.util.showToast
import com.zj.model.room.entity.Article
import com.zj.play.R
import com.zj.play.compose.MainActions
import com.zj.play.compose.common.article.ToTopButton
import com.zj.play.compose.common.lce.NoContent
import com.zj.play.compose.common.lce.SwipeArticleListPage
import com.zj.play.compose.common.signin.TextFieldState
import com.zj.play.compose.model.QueryArticle
import com.zj.play.compose.viewmodel.ArticleListViewModel
import com.zj.play.compose.viewmodel.BaseAndroidViewModel
import com.zj.play.compose.viewmodel.REFRESH_START
import dev.chrisbanes.accompanist.insets.statusBarsHeight

@Composable
fun SearchPage(
    actions: MainActions,
) {
    val searchState: TextFieldState = remember { TextFieldState() }
    val viewModel: ArticleListViewModel = viewModel()
    var clickSearchState by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    Scaffold(
        backgroundColor = colorResource(id = R.color.yellow),
        topBar = {
            SearchTitleBar(searchState, actions.upPress, {
                clickSearchState = true
                viewModel.getDataList(QueryArticle(1, k = searchState.text))
            })
        },
        content = {
            if (searchState.text.isBlank() || !clickSearchState) {
                NoContent()
            } else {
                SwipeArticleList(viewModel, listState, searchState, actions.enterArticle)
                ToTopButton(listState)
            }
        }
    )
}

@Composable
fun SwipeArticleList(
    viewModel: BaseAndroidViewModel<QueryArticle>,
    listState: LazyListState,
    searchState: TextFieldState,
    enterArticle: (Article) -> Unit,
) {
    SwipeArticleListPage(
        viewModel = viewModel,
        listState = listState,
        enterArticle = enterArticle,
        onRefresh = {
            viewModel.onPageChanged(0)
            viewModel.getDataList(QueryArticle(1, k = searchState.text))
            viewModel.onRefreshChanged(REFRESH_START)
        },
        onLoad = {
            viewModel.getDataList(
                QueryArticle(
                    (viewModel.page.value ?: 0) + 1,
                    k = searchState.text
                )
            )
            viewModel.onLoadRefreshStateChanged(REFRESH_START)
            viewModel.onPageChanged((viewModel.page.value ?: 0) + 1)
        },
        onErrorClick = {
            viewModel.getDataList(
                QueryArticle(
                    1,
                    k = searchState.text
                )
            )
        }) {
        NoContent("没有找到${searchState.text}相关内容")
    }
}

@Composable
fun SearchTitleBar(searchState: TextFieldState, upPress: () -> Unit, onSearch: () -> Unit) {
    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(Modifier.statusBarsHeight())
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
            // TODO hint
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
    }
}