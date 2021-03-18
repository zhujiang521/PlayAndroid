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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import com.zj.play.R
import com.zj.play.compose.MainActions
import com.zj.play.compose.common.PlayAppBar
import com.zj.play.compose.common.SwipeToRefreshAndLoadLayout
import com.zj.play.compose.common.article.ArticleItem
import com.zj.play.compose.common.article.PostCardPopular
import com.zj.play.compose.common.article.ToTopButton
import com.zj.play.compose.common.lce.BaseSwipeListPage
import com.zj.play.compose.common.lce.SetLcePage
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlayState
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.compose.viewmodel.HomePageViewModel
import com.zj.play.compose.viewmodel.REFRESH_START
import com.zj.play.compose.viewmodel.REFRESH_STOP

private const val TAG = "HomePage"

@Composable
fun HomePage(
    actions: MainActions,
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = viewModel()
) {

    val articleData by viewModel.dataLiveData.observeAsState(PlayLoading)

    val bannerData by viewModel.bannerState.observeAsState(PlayLoading)

    val refresh by viewModel.refreshState.observeAsState()

    val loadRefresh by viewModel.loadRefreshState.observeAsState()

    if (refresh != REFRESH_START && loadRefresh != REFRESH_START) {
        viewModel.getArticleList(isRefresh = false)
        viewModel.getBanner()
    }

    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        PlayAppBar(
            stringResource(id = R.string.home_page),
            false,
            showRight = true,
            rightImg = Icons.Rounded.Search,
            rightClick = actions.toSearch
        )

        BaseSwipeListPage(
            viewModel = viewModel,
            onRefresh = {
                Log.e(TAG, "onRefresh: 开始刷新")
                viewModel.onPageChanged(0)
                viewModel.getBanner()
                viewModel.getArticleList()
                viewModel.onRefreshChanged(REFRESH_START)
            },
            onLoad = {
                Log.e(TAG, "onLoad: 上拉加载")
                viewModel.onLoadRefreshStateChanged(REFRESH_START)
                viewModel.onPageChanged((viewModel.page.value ?: 0) + 1)
                viewModel.getArticleList()
            },
            onErrorClick = {
                viewModel.getArticleList()
                viewModel.getBanner()
            }) {
            SetLcePage(
                playState = bannerData,
                onErrorClick = {
                    viewModel.onPageChanged(0)
                    viewModel.getBanner()
                    viewModel.getArticleList()
                }
            ) {
                val data = bannerData as PlaySuccess<List<BannerBean>>
                Column {
                    LazyRow(
                        modifier = Modifier.padding(end = 16.dp),
                    ) {
                        items(data.data) {
                            PostCardPopular(
                                it,
                                actions.enterArticle,
                                Modifier.padding(start = 16.dp, bottom = 16.dp)
                            )
                        }
                    }
                    HomeArticleList(
                        modifier,
                        articleData,
                        listState,
                        viewModel,
                        actions.enterArticle
                    )
                }
            }


        }
    }

    if (articleData is PlaySuccess<*> && bannerData is PlaySuccess<*>) {
        ToTopButton(listState)
    }
}

@Composable
fun HomeArticleList(
    modifier: Modifier = Modifier,
    articleData: PlayState,
    listState: LazyListState,
    viewModel: HomePageViewModel,
    enterArticle: (Article) -> Unit
) {
    when (articleData) {
        PlayLoading -> {
            Spacer(modifier = Modifier.height(0.dp))
        }
        is PlaySuccess<*> -> {
            viewModel.onLoadRefreshStateChanged(REFRESH_STOP)
            viewModel.onRefreshChanged(REFRESH_STOP)
            val articleList = articleData as PlaySuccess<List<Article>>
            Log.e(
                TAG,
                "HomePage: articleData.data:${articleList.data.size}"
            )
            if (articleList.data.isEmpty()) {
                return
            }
            LazyColumn(
                modifier,
                state = listState
            ) {
                itemsIndexed(articleList.data) { index, article ->
                    ArticleItem(
                        article,
                        index,
                        enterArticle = { urlArgs ->
                            enterArticle(
                                urlArgs
                            )
                        })
                }
            }
        }
        is PlayError -> {
            Spacer(modifier = Modifier.height(0.dp))
            viewModel.onLoadRefreshStateChanged(REFRESH_STOP)
            viewModel.onRefreshChanged(REFRESH_STOP)
        }
    }
}
