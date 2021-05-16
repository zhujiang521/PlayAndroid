package com.zj.play.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.ExperimentalPagingApi
import com.zj.play.R
import com.zj.play.ui.page.home.HomePage
import com.zj.play.ui.page.mine.ProfilePage
import com.zj.play.ui.page.official.OfficialAccountPage
import com.zj.play.ui.page.project.ProjectPage
import java.util.*

@ExperimentalPagingApi
@Composable
fun MainPage(actions: PlayActions, viewModel: HomeViewModel = viewModel()) {

    val position by viewModel.position.observeAsState()
    val tabs = CourseTabs.values()

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        bottomBar = {
            BottomNavigation {
                tabs.forEach { tab ->
                    BottomNavigationItem(
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary),
                        icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                        label = { Text(stringResource(tab.title).toUpperCase(Locale.ROOT)) },
                        selected = tab == position,
                        onClick = {
                            viewModel.onPositionChanged(tab)
                        },
                        alwaysShowLabel = true,
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        // 淡入淡出布局切换动画
        Crossfade(targetState = position) { screen ->
            when (screen) {
                CourseTabs.HOME_PAGE -> HomePage(actions, modifier)
                CourseTabs.PROJECT -> ProjectPage(actions.enterArticle, modifier)
                CourseTabs.OFFICIAL_ACCOUNT -> OfficialAccountPage(actions.enterArticle, modifier)
                CourseTabs.MINE -> ProfilePage(actions)
            }
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
