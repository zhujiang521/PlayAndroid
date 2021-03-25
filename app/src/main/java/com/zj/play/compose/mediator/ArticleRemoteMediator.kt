/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.zj.play.compose.mediator

import com.zj.model.room.PlayDatabase
import com.zj.model.room.entity.*
import com.zj.network.base.ServiceCreator
import com.zj.network.service.HomePageService
import com.zj.network.service.OfficialService
import com.zj.network.service.ProjectService

class ProjectRemoteMediator(
    private val cid: Int,
    repoDatabase: PlayDatabase,
    private val service: ProjectService = ServiceCreator.create(ProjectService::class.java),
) : BaseRemoteMediator(PROJECT, repoDatabase) {

    override suspend fun getArticleList(page: Int): List<Article> {
        val apiResponse = service.getProject(page, cid)
        return apiResponse.data.datas
    }

}

class OfficialRemoteMediator(
    private val cid: Int,
    repoDatabase: PlayDatabase,
    private val service: OfficialService = ServiceCreator.create(OfficialService::class.java),
) : BaseRemoteMediator(OFFICIAL, repoDatabase) {

    override suspend fun getArticleList(page: Int): List<Article> {
        val apiResponse = service.getWxArticle(page, cid)
        return apiResponse.data.datas
    }

}

class HomeRemoteMediator(
    repoDatabase: PlayDatabase,
    private val service: HomePageService = ServiceCreator.create(HomePageService::class.java),
) : BaseRemoteMediator(HOME, repoDatabase) {

    override suspend fun getArticleList(page: Int): List<Article> {
        val apiResponse = service.getArticle(page)
        return apiResponse.data.datas
    }

}

class SearchRemoteMediator(
    private val keyword: String,
    repoDatabase: PlayDatabase,
    private val service: HomePageService = ServiceCreator.create(HomePageService::class.java),
) : BaseRemoteMediator(SEARCH, repoDatabase) {

    override suspend fun getArticleList(page: Int): List<Article> {
        val apiResponse = service.getQueryArticleList(page, keyword)
        return apiResponse.data.datas
    }

}