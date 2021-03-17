package com.zj.play.compose.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.core.util.showToast
import com.zj.model.room.entity.Article
import com.zj.play.R
import com.zj.play.compose.MainActions
import com.zj.play.compose.common.SwipeToRefreshAndLoadLayout
import com.zj.play.compose.common.article.ArticleItem
import com.zj.play.compose.common.lce.ErrorContent
import com.zj.play.compose.common.lce.LoadingContent
import com.zj.play.compose.common.lce.NoContent
import com.zj.play.compose.common.signin.TextFieldState
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.compose.viewmodel.ArticleListViewModel
import com.zj.play.compose.viewmodel.QueryKeyArticle
import com.zj.play.compose.viewmodel.REFRESH_START
import com.zj.play.compose.viewmodel.REFRESH_STOP
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.launch

@Composable
fun SearchPage(
    actions: MainActions,
) {
    val searchState: TextFieldState = remember { TextFieldState() }
    val viewModel: ArticleListViewModel = viewModel()
    val refresh by viewModel.refreshState.observeAsState()
    val loadRefresh by viewModel.loadRefreshState.observeAsState()
    val articleData by viewModel.dataLiveData.observeAsState(PlayLoading)
    var clickSearchState by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        backgroundColor = colorResource(id = R.color.yellow),
        topBar = {
            SearchTitleBar(searchState, actions.upPress, {
                clickSearchState = true
                viewModel.getDataList(QueryKeyArticle(1, searchState.text))
            })
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                if (searchState.text.isBlank() || !clickSearchState) {
                    NoContent()
                } else {
                    SwipeToRefreshAndLoadLayout(
                        refreshingState = refresh == REFRESH_START,
                        loadState = loadRefresh == REFRESH_START,
                        onRefresh = {
                            viewModel.onPageChanged(0)
                            viewModel.getDataList(QueryKeyArticle(1, searchState.text))
                            viewModel.onRefreshChanged(REFRESH_START)
                        },
                        onLoad = {
                            viewModel.getDataList(
                                QueryKeyArticle(
                                    (viewModel.page.value ?: 0) + 1,
                                    searchState.text
                                )
                            )
                            viewModel.onLoadRefreshStateChanged(REFRESH_START)
                            viewModel.onPageChanged((viewModel.page.value ?: 0) + 1)

                        },
                        content = {
                            when (articleData) {
                                PlayLoading -> {
                                    LoadingContent()
                                }
                                is PlayError -> {
                                    ErrorContent(enterArticle = {
                                        viewModel.getDataList(QueryKeyArticle(1, searchState.text))
                                    })
                                }
                                is PlaySuccess<*> -> {
                                    viewModel.onLoadRefreshStateChanged(REFRESH_STOP)
                                    viewModel.onRefreshChanged(REFRESH_STOP)
                                    val articleList =
                                        articleData as PlaySuccess<List<Article>>
                                    if (articleList.data.isEmpty()) {
                                        NoContent("没有找到${searchState.text}相关内容")
                                        return@SwipeToRefreshAndLoadLayout
                                    }
                                    LazyColumn(
                                        state = listState
                                    ) {
                                        itemsIndexed(articleList.data) { index, article ->
                                            ArticleItem(
                                                article,
                                                index,
                                                enterArticle = { urlArgs ->
                                                    actions.enterArticle(
                                                        urlArgs
                                                    )
                                                })
                                        }
                                    }
                                }
                            }
                        })

                }
                Button(modifier = Modifier
                    .padding(bottom = 65.dp, end = 8.dp)
                    .align(Alignment.BottomEnd), onClick = {
                    coroutineScope.launch {
                        // Animate scroll to the first item
                        listState.animateScrollToItem(index = 0)
                    }
                }) {
                    Image(
                        painter = painterResource(id = com.zj.play.R.drawable.img_to_top_nomal),
                        contentDescription = ""
                    )
                }
            }

        }
    )
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
                    .align(Alignment.CenterVertically)
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