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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.model.room.entity.Article
import com.zj.play.R
import com.zj.play.compose.common.ArticleItem
import com.zj.play.compose.common.ErrorContent
import com.zj.play.compose.common.LoadingContent
import com.zj.play.compose.common.PlayAppBar
import com.zj.play.compose.model.PlayError
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.home.HomePageViewModel
import dev.chrisbanes.accompanist.insets.statusBarsHeight

@Composable
fun HomePage(
    enterArticle: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = viewModel()
) {
    viewModel.getArticleList(1, true)
    val result by viewModel.state.observeAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.statusBarsHeight())
        PlayAppBar(stringResource(id = R.string.home_page), false)
        Log.e("ZHUJIANG123", "HomePage: ${result?.javaClass}")
        when (result) {
            PlayLoading -> {
                LoadingContent()
            }
            is PlaySuccess<*> -> {
                val data = result as PlaySuccess<List<Article>>
                LazyColumn(modifier) {
                    itemsIndexed(data.data) { index, article ->
                        ArticleItem(article, index, enterArticle)
                    }
                }
            }
            is PlayError -> {
                ErrorContent(enterArticle = { viewModel.getArticleList(1, true) })
            }
        }

    }

}
