/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zj.play.compose.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.ProjectClassify
import com.zj.play.R
import com.zj.play.compose.common.SwipeToRefreshAndLoadLayout
import com.zj.play.compose.common.article.ArticleItem
import com.zj.play.compose.common.lce.ErrorContent
import com.zj.play.compose.common.lce.LoadingContent
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.compose.viewmodel.*
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.launch

private const val TAG = "ProjectPage"

@Composable
fun ProjectPage(
    enterArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ProjectViewModel = viewModel()
    val projectViewModel: ProjectListViewModel = viewModel()
    ArticleListPage(
        enterArticle = enterArticle,
        modifier = modifier,
        viewModel = viewModel,
        projectViewModel = projectViewModel
    )
}


@Composable
fun ArticleListPage(
    enterArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BaseAndroidViewModel<Boolean>,
    projectViewModel: BaseAndroidViewModel<QueryArticle>
) {

    val listState = rememberLazyListState()
    // Remember a CoroutineScope to be able to launch
    val coroutineScope = rememberCoroutineScope()
    var loadState by remember { mutableStateOf(false) }
    var loadPageState by remember { mutableStateOf(false) }
    val refresh by viewModel.refreshState.observeAsState()
    val loadRefresh by viewModel.loadRefreshState.observeAsState()

    if (!loadState && refresh != REFRESH_START && loadRefresh != REFRESH_START) {
        viewModel.getDataList(false)
    }
    val result by viewModel.dataLiveData.observeAsState(PlayLoading)
    val position by viewModel.position.observeAsState()
    val articleList by projectViewModel.dataLiveData.observeAsState(PlayLoading)
    Box(modifier = Modifier.fillMaxSize()) {

        Column {
            Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
                Spacer(modifier = Modifier.statusBarsHeight())

                when (result) {
                    is PlayError -> {
                        ErrorContent(enterArticle = {
                            viewModel.getDataList(false)
                        })
                        loadState = true
                    }
                    PlayLoading -> {
                        LoadingContent()
                    }
                    is PlaySuccess<*> -> {
                        loadState = true
                        val data = result as PlaySuccess<List<ProjectClassify>>
                        ScrollableTabRow(
                            selectedTabIndex = position ?: 0,
                            modifier = Modifier.wrapContentWidth(),
                            edgePadding = 3.dp
                        ) {
                            data.data.forEachIndexed { index, projectClassify ->
                                Tab(
                                    text = { Text(projectClassify.name) },
                                    selected = position == index,
                                    onClick = {
                                        projectViewModel.getDataList(
                                            QueryArticle(
                                                0,
                                                projectClassify.id,
                                                true
                                            )
                                        )
                                        viewModel.onPositionChanged(index)
                                    }
                                )
                            }

                            if (position == 0 && !loadPageState) {
                                projectViewModel.getDataList(
                                    QueryArticle(
                                        projectViewModel.page.value ?: 0,
                                        data.data[0].id,
                                        true
                                    )
                                )
                            }

                        }

                        SwipeToRefreshAndLoadLayout(
                            refreshingState = refresh == REFRESH_START,
                            loadState = loadRefresh == REFRESH_START,
                            onRefresh = {
                                viewModel.onPageChanged(0)
                                viewModel.onRefreshChanged(REFRESH_START)
                                Log.e(TAG, "onRefresh: 开始刷新")
                                projectViewModel.getDataList(
                                    QueryArticle(
                                        0,
                                        data.data[position ?: 0].id,
                                        false
                                    )
                                )
                            },
                            onLoad = {
                                viewModel.onPageChanged((viewModel.page.value ?: 1) + 1)
                                viewModel.onLoadRefreshStateChanged(REFRESH_START)
                                projectViewModel.getDataList(
                                    QueryArticle(
                                        viewModel.page.value ?: 0,
                                        data.data[position ?: 0].id,
                                        true
                                    )
                                )
                            },
                            content = {
                                when (articleList) {
                                    is PlayLoading -> {
                                        LoadingContent()
                                        viewModel.onRefreshChanged(REFRESH_STOP)
                                    }
                                    is PlaySuccess<*> -> {
                                        viewModel.onRefreshChanged(REFRESH_STOP)
                                        viewModel.onLoadRefreshStateChanged(REFRESH_STOP)
                                        loadPageState = true
                                        val articles = articleList as PlaySuccess<List<Article>>
                                        LazyColumn(modifier, state = listState) {
                                            itemsIndexed(articles.data) { index, article ->
                                                ArticleItem(article, index, enterArticle)
                                            }
                                        }
                                    }
                                    is PlayError -> {
                                        viewModel.onLoadRefreshStateChanged(REFRESH_STOP)
                                        viewModel.onRefreshChanged(REFRESH_STOP)
                                        ErrorContent(enterArticle = {
                                            projectViewModel.getDataList(
                                                QueryArticle(
                                                    0,
                                                    data.data[position ?: 0].id,
                                                    true
                                                )
                                            )
                                        })
                                        loadPageState = true
                                    }
                                }
                            })

                    }
                }

            }

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
                painter = painterResource(id = R.drawable.img_to_top_nomal),
                contentDescription = ""
            )
        }

    }
}

