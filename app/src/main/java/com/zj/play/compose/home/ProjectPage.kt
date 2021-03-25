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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.ProjectClassify
import com.zj.play.compose.common.SwipeToRefreshLayout
import com.zj.play.compose.common.article.ArticleItem
import com.zj.play.compose.common.article.ToTopButton
import com.zj.play.compose.common.lce.SetLcePage
import com.zj.play.compose.common.lce.SwipeArticleListPage
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.compose.model.QueryArticle
import com.zj.play.compose.viewmodel.*
import dev.chrisbanes.accompanist.insets.statusBarsHeight

private const val TAG = "ProjectPage"

@Composable
fun ProjectPage(
    enterArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProjectViewModel = viewModel(),
    projectViewModel: ProjectListViewModel = viewModel(),
) {
    val listState = rememberLazyListState()
    var loadState by remember { mutableStateOf(false) }
    var loadPageState by remember { mutableStateOf(false) }

    val refresh by viewModel.refreshState.observeAsState()

    if (!loadState && refresh != REFRESH_START) {
        loadState = true
        viewModel.getDataList(false)
    }
    val tree by viewModel.dataLiveData.observeAsState(PlayLoading)
    val position by viewModel.position.observeAsState()

    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(modifier = Modifier.statusBarsHeight())
        SetLcePage(playState = tree, onErrorClick = {
            viewModel.getDataList(false)
            loadState = true
        }) {
            val data = (tree as PlaySuccess<List<ProjectClassify>>).data
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
                            projectViewModel.searchProject(projectClassify.id)
                            viewModel.onPositionChanged(index)
                        }
                    )
                }
                if (position == 0 && !loadPageState) {
                    loadPageState = true
                    projectViewModel.searchProject(data[position ?: 0].id)
                }
            }
            val lazyPagingItems = projectViewModel.repoResult.collectAsLazyPagingItems()
            LazyColumn(
                modifier = modifier,
                state = listState
            ) {

                if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                    item {
                        Text(
                            text = "Waiting for items to load from the backend",
                            modifier = Modifier.fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }

                itemsIndexed(lazyPagingItems) { index, article ->
                    ArticleItem(
                        article,
                        index,
                        enterArticle = { urlArgs ->
                            enterArticle(
                                urlArgs
                            )
                        })
                }

                if (lazyPagingItems.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            color = Color.Magenta
                        )
                    }
                }

            }
        }
    }

    if (tree is PlaySuccess<*>) {
        ToTopButton(listState)
    }
}


@Composable
fun ArticleListPage(
    modifier: Modifier,
    enterArticle: (Article) -> Unit,
    viewModel: BaseAndroidViewModel<Boolean>,
    projectViewModel: BaseAndroidViewModel<QueryArticle>
) {

    val listState = rememberLazyListState()
    var loadState by remember { mutableStateOf(false) }
    var loadPageState by remember { mutableStateOf(false) }
    val refresh by viewModel.refreshState.observeAsState()
    val loadRefresh by viewModel.loadRefreshState.observeAsState()

    if (!loadState && refresh != REFRESH_START && loadRefresh != REFRESH_START) {
        loadState = true
        viewModel.getDataList(false)
    }
    val result by viewModel.dataLiveData.observeAsState(PlayLoading)
    val position by viewModel.position.observeAsState()

    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(modifier = Modifier.statusBarsHeight())
        SetLcePage(playState = result, onErrorClick = {
            viewModel.getDataList(false)
            loadState = true
        }) {
            loadState = true
            val data = (result as PlaySuccess<List<ProjectClassify>>).data
            ArticleTabRow(data, loadPageState, position, viewModel, projectViewModel) {
                loadPageState = true
            }
            ArticleSwipeList(
                modifier,
                data,
                projectViewModel,
                position,
                listState,
                enterArticle,
            ) {
                loadPageState = true
            }
        }
    }

    if (result is PlaySuccess<*>) {
        ToTopButton(listState)
    }

}

@Composable
fun ArticleSwipeList(
    modifier: Modifier,
    data: List<ProjectClassify>,
    projectViewModel: BaseAndroidViewModel<QueryArticle>,
    position: Int?,
    listState: LazyListState,
    enterArticle: (Article) -> Unit,
    onErrorClick: () -> Unit,
) {
    SwipeArticleListPage(
        modifier = modifier,
        viewModel = projectViewModel,
        listState = listState,
        enterArticle = enterArticle,
        onRefresh = {
            projectViewModel.onPageChanged(0)
            projectViewModel.onRefreshChanged(REFRESH_START)
            Log.e(TAG, "onRefresh: 开始刷新")
            projectViewModel.getDataList(
                QueryArticle(
                    0,
                    data[position ?: 0].id,
                    false
                )
            )
        },
        onLoad = {
            projectViewModel.onLoadRefreshStateChanged(REFRESH_START)
            projectViewModel.onPageChanged((projectViewModel.page.value ?: 1) + 1)
            projectViewModel.getDataList(
                QueryArticle(
                    (projectViewModel.page.value ?: 0) + 1,
                    data[position ?: 0].id,
                    false,
                    isLoad = true
                )
            )
        },
        onErrorClick = {
            projectViewModel.onLoadRefreshStateChanged(REFRESH_STOP)
            projectViewModel.onRefreshChanged(REFRESH_STOP)
            projectViewModel.getDataList(
                QueryArticle(
                    0,
                    data[position ?: 0].id,
                    false
                )
            )
            onErrorClick()
        }) {}
}

@Composable
fun ArticleTabRow(
    data: List<ProjectClassify>,
    loadPageState: Boolean,
    position: Int?,
    viewModel: BaseAndroidViewModel<Boolean>,
    projectViewModel: BaseAndroidViewModel<QueryArticle>,
    onLoadFinish: () -> Unit
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
                    Log.e("ZHUJIANG123", "ArticleTabRow: projectClassify.id:${projectClassify.id}")
                    projectViewModel.getDataList(
                        QueryArticle(
                            0,
                            projectClassify.id,
                            false
                        )
                    )
                    viewModel.onPositionChanged(index)
                }
            )
        }

        if (position == 0 && !loadPageState) {
            onLoadFinish()
            val value = projectViewModel.dataLiveData.value
            if (value !is PlaySuccess<*>) {
                getDataList(projectViewModel, data)
            } else {
                val articleList =
                    value as PlaySuccess<List<Article>>
                if (articleList.data.isEmpty()) {
                    getDataList(projectViewModel, data)
                }
            }

        }
    }
}

fun getDataList(projectViewModel: BaseAndroidViewModel<QueryArticle>, data: List<ProjectClassify>) {
    projectViewModel.getDataList(
        QueryArticle(
            projectViewModel.page.value ?: 0,
            data[0].id,
            false
        )
    )
}

