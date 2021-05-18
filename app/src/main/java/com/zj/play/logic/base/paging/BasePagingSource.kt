/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zj.play.logic.base.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.network.PlayAndroidNetwork

abstract class BasePagingSource : PagingSource<Int, ArticleModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleModel> {
        return try {
            val page = params.key ?: 1 // set page 1 as default
            val articleList = getArticleList(page)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (articleList.isNotEmpty()) page + 1 else null
            LoadResult.Page(articleList, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleModel>): Int? =null

    abstract suspend fun getArticleList(page: Int): List<ArticleModel>
}