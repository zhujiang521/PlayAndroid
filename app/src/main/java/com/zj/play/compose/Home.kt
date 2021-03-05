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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zj.play.R
import com.zj.play.compose.home.HomePage
import com.zj.play.compose.home.OfficialAccountPage
import com.zj.play.compose.home.ProfilePage
import com.zj.play.compose.home.ProjectPage
import com.zj.play.compose.viewmodel.HomeViewModel
import dev.chrisbanes.accompanist.insets.navigationBarsHeight
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import java.util.*

@Composable
fun Home(enterArticle: (String) -> Unit, toLogin: () -> Unit) {
    val viewModel: HomeViewModel = viewModel()
    val position by viewModel.position.observeAsState()
    val tabs = CourseTabs.values()

    Scaffold(
        backgroundColor = colorResource(id = R.color.yellow),
        bottomBar = {
            BottomNavigation(
                Modifier.navigationBarsHeight(additional = 56.dp)
            ) {
                tabs.forEach { tab ->
                    BottomNavigationItem(
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary)
                            .navigationBarsPadding(),
                        icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                        label = { Text(stringResource(tab.title).toUpperCase(Locale.ROOT)) },
                        selected = tab == position,
                        onClick = { //setSelectedTab(tab)
                            viewModel.onPositionChanged(tab)
                        },
                        alwaysShowLabel = false,
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (position) {
            CourseTabs.HOME_PAGE -> HomePage(enterArticle, modifier)
            CourseTabs.PROJECT -> ProjectPage(enterArticle, modifier)
            CourseTabs.OFFICIAL_ACCOUNT -> OfficialAccountPage(enterArticle, modifier)
            CourseTabs.MINE -> ProfilePage(toLogin)
        }
    }
}

enum class CourseTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    HOME_PAGE(R.string.home_page, R.drawable.ic_nav_news_normal),
    PROJECT(R.string.project, R.drawable.ic_nav_tweet_normal),
    OFFICIAL_ACCOUNT(R.string.official_account, R.drawable.ic_nav_discover_normal),
    MINE(R.string.mine, R.drawable.ic_nav_my_normal)
}
