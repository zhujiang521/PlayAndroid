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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.model.pojo.QueryArticle
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.ProjectClassify
import com.zj.play.R
import com.zj.play.compose.common.ArticleItem
import com.zj.play.compose.common.ErrorContent
import com.zj.play.compose.common.LoadingContent
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.home.REFRESH_STOP
import com.zj.play.official.OfficialViewModel
import com.zj.play.official.list.OfficialListViewModel
import dev.chrisbanes.accompanist.insets.statusBarsHeight

@Composable
fun OfficialAccountPage(
    enterArticle: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OfficialViewModel = viewModel(),
    officialListViewModel: OfficialListViewModel = viewModel()
) {
    var loadState by remember { mutableStateOf(false) }
    var loadPageState by remember { mutableStateOf(false) }

    if (!loadState) {
        viewModel.getDataList(false)
    }
    val result by viewModel.dataLiveData.observeAsState()
    val position = viewModel.position.observeAsState()
    val articleList by officialListViewModel.dataLiveData.observeAsState()

    Column {
        Column(modifier = Modifier.background(color = colorResource(id = R.color.yellow))) {
            Spacer(modifier = Modifier.statusBarsHeight())
            ScrollableTabRow(
                selectedTabIndex = position.value ?: 0,
                modifier = Modifier.wrapContentWidth(),
                edgePadding = 3.dp
            ) {
                when (result) {
                    is PlayError, null -> {
                        ErrorContent(enterArticle = { })
                        loadState = true
                    }
                    PlayLoading -> LoadingContent()
                    is PlaySuccess<*> -> {
                        loadState = true
                        val data = result as PlaySuccess<List<ProjectClassify>>
                        data.data.forEachIndexed { index, projectClassify ->
                            Tab(
                                text = { Text(projectClassify.name) },
                                selected = position.value == index,
                                onClick = {
                                    officialListViewModel.getDataList(
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

                        if (position.value == 0 && !loadPageState) {
                            officialListViewModel.getDataList(
                                QueryArticle(
                                    0,
                                    data.data[0].id,
                                    true
                                )
                            )
                        }

                    }
                }
            }

            when (articleList) {
                is PlayLoading -> {
                    LoadingContent()
                }
                is PlaySuccess<*> -> {
                    loadPageState = true
                    val articles = articleList as PlaySuccess<List<Article>>
                    LazyColumn(modifier) {
                        if (articleList == null) {
                            item {
                                ErrorContent(enterArticle = { /*TODO*/ })
                            }
                            return@LazyColumn
                        }
                        itemsIndexed(articles.data) { index, article ->
                            ArticleItem(article, index, enterArticle)
                        }
                    }
                }
                is PlayError -> {
                    ErrorContent(enterArticle = { })
                    loadPageState = true
                }
            }

        }

    }

}
