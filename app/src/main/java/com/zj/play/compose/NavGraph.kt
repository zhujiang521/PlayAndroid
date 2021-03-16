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

package com.zj.play.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.google.gson.Gson
import com.zj.core.util.getHtmlText
import com.zj.model.room.entity.Article
import com.zj.play.compose.MainDestinations.ARTICLE_ROUTE_URL
import com.zj.play.compose.common.article.ArticlePage
import com.zj.play.compose.home.AboutMePage
import com.zj.play.compose.home.LoginPage
import com.zj.play.compose.home.SearchPage
import com.zj.play.compose.theme.Play2Theme
import com.zj.play.compose.theme.PlayTheme
import com.zj.play.compose.viewmodel.ThemeViewModel
import java.net.URLEncoder

/**
 * Destinations used in the ([NewMainActivity]).
 */
object MainDestinations {
    const val HOME_PAGE_ROUTE = "home_page_route"
    const val ARTICLE_ROUTE = "article_route"
    const val ARTICLE_ROUTE_URL = "article_route_url"
    const val LOGIN_ROUTE = "login_route"
    const val SEARCH_ROUTE = "search_route"
    const val ABOUT_ME_ROUTE = "about_me_route"
}

@Composable
fun NavGraphPage() {
    val viewModel: ThemeViewModel = viewModel()
    val theme by viewModel.theme.observeAsState()
    if (theme == false) {
        PlayTheme {
            NavGraph(viewModel)
        }
    } else {
        Play2Theme {
            NavGraph(viewModel)
        }
    }
}

@Composable
fun NavGraph(
    viewModel: ThemeViewModel,
    startDestination: String = MainDestinations.HOME_PAGE_ROUTE
) {
    val navController = rememberNavController()

    val actions = remember(navController) { MainActions(navController) }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestinations.HOME_PAGE_ROUTE) {
            Home(actions, viewModel)
        }
        composable(MainDestinations.LOGIN_ROUTE) {
            LoginPage(actions)
        }
        composable(
            "${MainDestinations.ARTICLE_ROUTE}/{$ARTICLE_ROUTE_URL}",
            arguments = listOf(navArgument(ARTICLE_ROUTE_URL) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val parcelable = arguments.getString(ARTICLE_ROUTE_URL)
            val fromJson = Gson().fromJson(parcelable, Article::class.java)
            ArticlePage(
                article = fromJson,
                onBack = actions.upPress
            )
        }
        composable(MainDestinations.SEARCH_ROUTE) {
            SearchPage(actions)
        }
        composable(MainDestinations.ABOUT_ME_ROUTE) {
            AboutMePage(actions)
        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val homePage: () -> Unit = {
        navController.navigate(MainDestinations.HOME_PAGE_ROUTE)
    }
    val enterArticle: (Article) -> Unit = { article ->
        article.desc = ""
        article.title = getHtmlText(article.title)
        val gson = Gson().toJson(article).trim()
        navController.navigate("${MainDestinations.ARTICLE_ROUTE}/$gson")
    }
    val toLogin: () -> Unit = {
        navController.navigate(MainDestinations.LOGIN_ROUTE)
    }
    val toSearch: () -> Unit = {
        navController.navigate(MainDestinations.SEARCH_ROUTE)
    }
    val toAboutMe: () -> Unit = {
        navController.navigate(MainDestinations.ABOUT_ME_ROUTE)
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
