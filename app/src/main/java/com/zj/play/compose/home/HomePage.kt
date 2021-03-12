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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import com.zj.play.R
import com.zj.play.compose.common.article.ArticleItem
import com.zj.play.compose.common.lce.ErrorContent
import com.zj.play.compose.common.lce.LoadingContent
import com.zj.play.compose.common.PlayAppBar
import com.zj.play.compose.common.SwipeToLoadLayout
import com.zj.play.compose.common.SwipeToRefreshAndLoadLayout
import com.zj.play.compose.common.SwipeToRefreshLayout
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.compose.common.article.PostCardPopular
import com.zj.play.compose.viewmodel.HomePageViewModel
import com.zj.play.compose.viewmodel.REFRESH_DEFAULT
import com.zj.play.compose.viewmodel.REFRESH_START
import com.zj.play.compose.viewmodel.REFRESH_STOP

private const val TAG = "HomePage"

@Composable
fun HomePage(
    enterArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = viewModel()
) {
    var loadState by remember { mutableStateOf(false) }

    val result by viewModel.state.observeAsState(PlayLoading)

    val banner by viewModel.bannerState.observeAsState(PlayLoading)

    val refresh by viewModel.refreshState.observeAsState()

    val loadRefresh by viewModel.loadRefreshState.observeAsState()

    Log.e(TAG, "HomePage:refresh : $refresh")

    if (!loadState && refresh != REFRESH_START && loadRefresh != REFRESH_START) {
        viewModel.getArticleList(1, false)
        viewModel.getBanner()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PlayAppBar(stringResource(id = R.string.home_page), false)

        SwipeToRefreshAndLoadLayout(
            refreshingState = refresh == REFRESH_START,
            loadState = loadRefresh == REFRESH_START,
            onRefresh = {
                Log.e(TAG, "onRefresh: 开始刷新")
                viewModel.getBanner()
                viewModel.getArticleList(1, true)
                viewModel.onRefreshChanged(REFRESH_START)
            },
            onLoad = {
                Log.e("ZHUJIANG123", "onLoad: 222")
                viewModel.onLoadRefreshStateChanged(REFRESH_START)
                viewModel.getBanner()
                viewModel.getArticleList(1, true)
            },
            content = {

                when (banner) {
                    PlayLoading -> {
                        LoadingContent()
                    }
                    is PlayError -> {
                        ErrorContent(enterArticle = {
                            viewModel.getArticleList(1, true)
                            viewModel.getBanner()
                        })
                    }
                    is PlaySuccess<*> -> {
                        val data = banner as PlaySuccess<List<BannerBean>>
                        Column {
                            LazyRow(modifier = Modifier.padding(end = 16.dp)) {
                                items(data.data) {
                                    PostCardPopular(
                                        it,
                                        enterArticle,
                                        Modifier.padding(start = 16.dp, bottom = 16.dp)
                                    )
                                }
                            }
                            when (result) {
                                PlayLoading -> {
                                    Spacer(modifier = Modifier.height(0.dp))
                                }
                                is PlaySuccess<*> -> {
                                    loadState = true
                                    viewModel.onLoadRefreshStateChanged(REFRESH_STOP)
                                    viewModel.onRefreshChanged(REFRESH_STOP)
                                    val articleData = result as PlaySuccess<List<Article>>
                                    LazyColumn(modifier) {
                                        itemsIndexed(articleData.data) { index, article ->
                                            ArticleItem(
                                                article,
                                                index,
                                                enterArticle = { urlArgs -> enterArticle(urlArgs) })
                                        }
                                    }
                                }
                                is PlayError -> {
                                    Spacer(modifier = Modifier.height(0.dp))
                                    loadState = true
                                    viewModel.onLoadRefreshStateChanged(REFRESH_STOP)
                                    viewModel.onRefreshChanged(REFRESH_STOP)
                                }
                            }
                        }
                    }
                }
            })
    }

}