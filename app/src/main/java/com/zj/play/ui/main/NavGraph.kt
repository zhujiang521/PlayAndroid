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

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.zj.model.ArticleModel
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.main.nav.PlayDestinations
import com.zj.play.ui.main.nav.PlayDestinations.ARTICLE_ROUTE_URL
import com.zj.play.ui.main.nav.PlayDestinations.SYSTEM_ARTICLE_LIST_ROUTE_CID
import com.zj.play.ui.main.nav.PlayDestinations.SYSTEM_ARTICLE_LIST_ROUTE_NAME
import com.zj.play.ui.page.article.ArticlePage
import com.zj.play.ui.page.login.LoginPage
import com.zj.play.ui.page.mine.ThemePage
import com.zj.play.ui.page.search.SearchPage
import com.zj.play.ui.page.system.SystemArticleListPage
import com.zj.play.ui.page.system.SystemViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    startDestination: String = PlayDestinations.HOME_PAGE_ROUTE,
    articleModel: ArticleModel? = null
) {
    val navController = rememberNavController()
    val actions = remember(navController) { PlayActions(navController) }
    NavHost(navController = navController, startDestination = startDestination) {
        composableHorizontal(PlayDestinations.HOME_PAGE_ROUTE) {
            MainPage(actions)
        }
        composableVertical(PlayDestinations.SEARCH_PAGE_ROUTE) {
            SearchPage(actions)
        }
        composableHorizontal(PlayDestinations.LOGIN_ROUTE) {
            LoginPage(actions)
        }
        composableHorizontal(PlayDestinations.THEME_ROUTE) {
            ThemePage(actions)
        }
        composableHorizontal(
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
        composableHorizontal(
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
    if (articleModel != null) {
        actions.enterArticle(articleModel)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableHorizontal(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    this@composableHorizontal.composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            // Let's make for a really long fade in
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        content = content,
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
    )
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableVertical(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    this@composableVertical.composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            // Let's make for a really long fade in
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(300)
            )
        },
        content = content,
    )
}