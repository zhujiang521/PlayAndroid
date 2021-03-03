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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.model.pojo.QueryArticle
import com.zj.play.compose.common.ArticleItem
import com.zj.play.compose.common.ErrorContent
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
    viewModel.getDataList(false)
    val result by viewModel.dataLiveData.observeAsState()
    val position = viewModel.position.observeAsState()
    val articleList by officialListViewModel.dataLiveData.observeAsState()
    if (result == null) {
        ErrorContent(enterArticle = { /*TODO*/ })
        return
    }

    if (position.value == 0) {
        officialListViewModel.getDataList(
            QueryArticle(
                0,
                result!![0].id,
                true
            )
        )
    }

    Column {
        Spacer(modifier = Modifier.statusBarsHeight())
        ScrollableTabRow(
            selectedTabIndex = position.value ?: 0,
            modifier = Modifier.wrapContentWidth(),
            edgePadding = 3.dp
        ) {
            result!!.forEachIndexed { index, projectClassify ->
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
        }

        LazyColumn(modifier) {
            if (articleList == null) {
                item {
                    ErrorContent(enterArticle = { /*TODO*/ })
                }
                return@LazyColumn
            }
            itemsIndexed(articleList!!) { index, article ->
                ArticleItem(article, index, enterArticle)
            }
        }

    }

}
