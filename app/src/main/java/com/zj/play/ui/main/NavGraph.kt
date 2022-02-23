/*
 * Copyright 2021 The Android Open Source Project
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

package com.zj.play.ui.main

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.gson.Gson
import com.zj.play.logic.model.ArticleModel
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.main.nav.PlayDestinations
import com.zj.play.ui.main.nav.PlayDestinations.ARTICLE_ROUTE_URL
import com.zj.play.ui.main.nav.PlayDestinations.SYSTEM_ARTICLE_LIST_ROUTE_CID
import com.zj.play.ui.main.nav.PlayDestinations.SYSTEM_ARTICLE_LIST_ROUTE_NAME
import com.zj.play.ui.page.article.ArticlePage
import com.zj.play.ui.page.login.LoginPage
import com.zj.play.ui.page.search.SearchPage
import com.zj.play.ui.page.system.SystemArticleListPage
import com.zj.play.ui.page.system.SystemViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    startDestination: String = PlayDestinations.HOME_PAGE_ROUTE
) {
    val navController = rememberAnimatedNavController()
    val actions = remember(navController) { PlayActions(navController) }
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        setComposable(PlayDestinations.HOME_PAGE_ROUTE) {
            MainPage(actions)
        }
        setComposable(PlayDestinations.SEARCH_PAGE_ROUTE) {
            SearchPage(actions)
        }
        setComposable(PlayDestinations.LOGIN_ROUTE) {
            LoginPage(actions)
        }
        setComposable(
            "${PlayDestinations.SYSTEM_ARTICLE_LIST_ROUTE}/{$SYSTEM_ARTICLE_LIST_ROUTE_CID}/{$SYSTEM_ARTICLE_LIST_ROUTE_NAME}",
            arguments = listOf(navArgument(SYSTEM_ARTICLE_LIST_ROUTE_CID) {
                type = NavType.IntType
            }, navArgument(SYSTEM_ARTICLE_LIST_ROUTE_NAME) {
                type = NavType.StringType
            }),
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val cid = arguments.getInt(SYSTEM_ARTICLE_LIST_ROUTE_CID)
            val name = arguments.getString(SYSTEM_ARTICLE_LIST_ROUTE_NAME)
            val viewModel: SystemViewModel = hiltViewModel()
            viewModel.getSystemArticle(cid)
            SystemArticleListPage(name, viewModel, actions)
        }
        setComposable(
            "${PlayDestinations.ARTICLE_ROUTE}/{$ARTICLE_ROUTE_URL}",
            arguments = listOf(navArgument(ARTICLE_ROUTE_URL) {
                type = NavType.StringType
            }),
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val parcelable = arguments.getString(ARTICLE_ROUTE_URL)
            val fromJson = Gson().fromJson(parcelable, ArticleModel::class.java)
            ArticlePage(
                article = fromJson,
                onBack = actions.upPress
            )
        }
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.setComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            // Let's make for a really long fade in
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
//        exitTransition = { _, _ ->
//            // Let's make for a really long fade in
//            slideOutOfContainer(
//                AnimatedContentScope.SlideDirection.Left,
//                animationSpec = tween(300)
//            )
//        },
        content = content,
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
//        popExitTransition = { _, _ ->
//            slideOutOfContainer(
//                AnimatedContentScope.SlideDirection.Right,
//                animationSpec = tween(300)
//            )
//        }
    )
}