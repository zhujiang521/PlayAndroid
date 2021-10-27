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

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.gson.Gson
import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.model.PlayLoading
import com.zj.play.logic.model.PlaySuccess
import com.zj.play.logic.utils.getHtmlText
import com.zj.play.logic.utils.showToast
import com.zj.play.ui.main.PlayDestinations.ARTICLE_ROUTE_URL
import com.zj.play.ui.main.PlayDestinations.SYSTEM_ARTICLE_LIST_ROUTE_CID
import com.zj.play.ui.main.PlayDestinations.SYSTEM_ARTICLE_LIST_ROUTE_NAME
import com.zj.play.ui.page.article.ArticlePage
import com.zj.play.ui.page.login.LoginPage
import com.zj.play.ui.page.login.LoginViewModel
import com.zj.play.ui.page.search.SearchPage
import com.zj.play.ui.page.search.SearchViewModel
import com.zj.play.ui.page.system.SystemArticleListPage
import com.zj.play.ui.page.system.SystemViewModel
import java.net.URLEncoder
import java.util.*


object PlayDestinations {
    const val HOME_PAGE_ROUTE = "home_page_route"
    const val ARTICLE_ROUTE = "article_route"
    const val ARTICLE_ROUTE_URL = "article_route_url"
    const val LOGIN_ROUTE = "login_route"
    const val SYSTEM_ARTICLE_LIST_ROUTE = "system_article_list_route"
    const val SYSTEM_ARTICLE_LIST_ROUTE_CID = "system_article_list_route_cid"
    const val SYSTEM_ARTICLE_LIST_ROUTE_NAME = "system_article_list_route_name"
    const val SEARCH_PAGE_ROUTE = "search_page_route"
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    startDestination: String = PlayDestinations.HOME_PAGE_ROUTE
) {
    val navController = rememberAnimatedNavController()
    val current = LocalContext.current
    val actions = remember(navController) { PlayActions(navController) }
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        setComposable(
            PlayDestinations.HOME_PAGE_ROUTE,
        ) {
            val viewModel: HomeViewModel = viewModel()
            val position by viewModel.position.observeAsState()
            MainPage(viewModel, actions, position) { tab ->
                viewModel.onPositionChanged(tab)
            }
        }
        setComposable(
            PlayDestinations.SEARCH_PAGE_ROUTE,
        ) {
            val viewModel = viewModel<SearchViewModel>()
            val lazyPagingItems = viewModel.articleResult.collectAsLazyPagingItems()
            val hotkeyModels by viewModel.hotkeyState.observeAsState(PlayLoading)
            SearchPage(back = actions.upPress,
                lazyPagingItems = lazyPagingItems,
                hotkeyModels = hotkeyModels,
                loadHotkey = {
                    if (hotkeyModels !is PlaySuccess<*>) {
                        viewModel.getHotkeyList()
                    }
                },
                enterArticle = {
                    actions.enterArticle(it)
                },
                searchArticle = {
                    if (it.isEmpty()) {
                        showToast(current, "请输入搜索内容")
                        return@SearchPage
                    }
                    viewModel.getSearchArticle(it)
                })
        }
        setComposable(
            PlayDestinations.LOGIN_ROUTE,
        ) {
            val viewModel: LoginViewModel = viewModel()
            val loginState by viewModel.state.observeAsState()
            LoginPage(actions, loginState, {
                viewModel.logout()
            }) {
                viewModel.toLoginOrRegister(it)
            }
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
            val viewModel: SystemViewModel = viewModel()
            val lazyPagingItems = viewModel.articleResult.collectAsLazyPagingItems()
            viewModel.getSystemArticle(cid)
            SystemArticleListPage(name, back = { actions.upPress() }, lazyPagingItems) {
                actions.enterArticle(it)
            }
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
        enterTransition = { _, _ ->
            // Let's make for a really long fade in
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(500)
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
        popEnterTransition = { _, _ ->
            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
        },
//        popExitTransition = { _, _ ->
//            slideOutOfContainer(
//                AnimatedContentScope.SlideDirection.Right,
//                animationSpec = tween(300)
//            )
//        }
    )
}

/**
 * 对应用程序中的导航操作进行建模。
 */
class PlayActions(navController: NavHostController) {

    val enterArticle: (ArticleModel) -> Unit = { article ->
        article.desc = ""
        article.title = getHtmlText(article.title)
        val gson = Gson().toJson(article).trim()
        val result = URLEncoder.encode(gson, "utf-8")
        navController.navigate("${PlayDestinations.ARTICLE_ROUTE}/$result")
    }

    val toSystemArticleList: (Int, String) -> Unit = { cid, name ->
        navController.navigate("${PlayDestinations.SYSTEM_ARTICLE_LIST_ROUTE}/$cid/$name")
    }

    val toLogin: () -> Unit = {
        navController.navigate(PlayDestinations.LOGIN_ROUTE)
    }

    val toSearch: () -> Unit = {
        navController.navigate(PlayDestinations.SEARCH_PAGE_ROUTE)
    }

    val upPress: () -> Unit = {
        navController.navigateUp()
    }

}


