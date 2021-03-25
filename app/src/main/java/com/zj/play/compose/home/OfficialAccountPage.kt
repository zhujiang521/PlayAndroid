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

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.ExperimentalPagingApi
import com.zj.model.room.entity.Article
import com.zj.play.compose.viewmodel.OfficialViewModel
import com.zj.play.compose.viewmodel.OfficialListViewModel

@ExperimentalPagingApi
@Composable
fun OfficialAccountPage(
    enterArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OfficialViewModel = viewModel(),
    officialListViewModel: OfficialListViewModel = viewModel()
) {
    ArticleListPage(
        modifier = modifier,
        enterArticle = enterArticle,
        viewModel = viewModel,
        articleViewModel = officialListViewModel
    )
}
