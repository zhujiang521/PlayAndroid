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
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.zj.play.compose.MainDestinations.ARTICLE_ROUTE_URL
import com.zj.play.compose.common.article.ArticlePage

/**
 * Destinations used in the ([NewMainActivity]).
 */
object MainDestinations {

    const val HOME_PAGE_ROUTE = "home_page_route"
    const val ARTICLE_ROUTE = "article_route"
    const val ARTICLE_ROUTE_URL = "article_route_url"
//    const val PROJECT_ROUTE = "project_route"
//    const val OFFICIAL_ROUTE = "official_route"
//    const val MY_INFORMATION_ROUTE = "my_information_route"
}

@Composable
fun NavGraph(startDestination: String = MainDestinations.HOME_PAGE_ROUTE) {
    val navController = rememberNavController()

    val actions = remember(navController) { MainActions(navController) }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestinations.HOME_PAGE_ROUTE) {
            Home(enterArticle = actions.enterArticle)
        }
        composable(
            "${MainDestinations.ARTICLE_ROUTE}/{$ARTICLE_ROUTE_URL}",
            arguments = listOf(navArgument(ARTICLE_ROUTE_URL) { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            ArticlePage(
                url = arguments.getString(ARTICLE_ROUTE_URL) ?: "www.baidu.com",
                onBack = actions.upPress
            )
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
    val enterArticle: (String) -> Unit = { url ->
        navController.navigate("${MainDestinations.ARTICLE_ROUTE}/$url")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
