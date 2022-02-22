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

import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.network.PlayAndroidNetwork
import com.zj.play.logic.utils.XLog

class SystemPagingSource(private val cid: Int) : BasePagingSource() {

    companion object {
        private const val TAG = "SystemPagingSource"
    }

    override suspend fun getArticleList(page: Int): List<ArticleModel> {
        XLog.e(TAG, "getArticleList: page:$page   cid:$cid")
        val apiResponse = PlayAndroidNetwork.getSystemArticle(page, cid)
        return apiResponse.data.datas
    }
}