package com.zj.play.ui.main

import android.content.res.Configuration
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.zj.play.R
import com.zj.play.logic.model.AndroidSystemModel
import com.zj.play.logic.model.PlayLoading
import com.zj.play.logic.model.PlaySuccess
import com.zj.play.ui.page.home.HomePage
import com.zj.play.ui.page.login.LoginViewModel
import com.zj.play.ui.page.login.LogoutDefault
import com.zj.play.ui.page.mine.ProfilePage
import com.zj.play.ui.page.official.OfficialAndroidViewModel
import com.zj.play.ui.page.project.ArticleListPage
import com.zj.play.ui.page.project.ProjectAndroidViewModel
import com.zj.play.ui.page.system.SystemPage
import com.zj.play.ui.page.system.SystemViewModel
import java.util.*

@Composable
fun MainPage(
    homeViewModel: HomeViewModel,
    actions: PlayActions, position: CourseTabs?,
    onPositionChanged: (CourseTabs) -> Unit
) {
    val tabs = CourseTabs.values()
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        bottomBar = {
            BottomNavigation {
                tabs.forEach { tab ->
                    BottomNavigationItem(
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary),
                        icon = {
                            val painter: Painter = if (tab == position) {
                                painterResource(tab.selectIcon)
                            } else {
                                painterResource(tab.icon)
                            }
                            Icon(painter, contentDescription = null)
                        },
                        label = { Text(stringResource(tab.title).uppercase(Locale.ROOT)) },
                        selected = tab == position,
                        onClick = {
                            onPositionChanged(tab)
                        },
                        alwaysShowLabel = true,
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        // 当前是否为横屏
        val isLand = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
        // 淡入淡出布局切换动画
        Crossfade(targetState = position) { screen ->
            when (screen) {
                CourseTabs.HOME_PAGE -> {
                    val bannerData by homeViewModel.bannerState.observeAsState(PlayLoading)
                    val lazyPagingItems = homeViewModel.articleResult.collectAsLazyPagingItems()
                    HomePage(modifier, isLand, bannerData, lazyPagingItems, {
                        if (bannerData !is PlaySuccess<*>) {
                            homeViewModel.getBanner()
                        }
                        if (lazyPagingItems.itemCount <= 0) {
                            homeViewModel.getHomeArticle()
                        }
                    }, toSearch = {
                        actions.toSearch()
                    }) {
                        actions.enterArticle(it)
                    }
                }
                CourseTabs.SYSTEM -> {
                    val viewModel: SystemViewModel = viewModel()
                    val androidSystemState by viewModel.androidSystemState.observeAsState(
                        PlayLoading
                    )
                    val systemState by viewModel.systemState.observeAsState(
                        AndroidSystemModel(
                            arrayListOf()
                        )
                    )
                    SystemPage(modifier, androidSystemState, systemState,
                        saveSystemState = {
                            viewModel.onSystemModelChanged(it)
                        }, loadData = {
                            viewModel.getAndroidSystem()
                        }) { cid, name ->
                        actions.toSystemArticleList(cid, name)
                    }
                }
                CourseTabs.PROJECT, CourseTabs.OFFICIAL_ACCOUNT -> {
                    val viewModel = if (screen == CourseTabs.PROJECT) {
                        viewModel<ProjectAndroidViewModel>()
                    } else {
                        viewModel<OfficialAndroidViewModel>()
                    }
                    val lazyPagingItems = viewModel.articleResult.collectAsLazyPagingItems()
                    val tree by viewModel.treeLiveData.observeAsState(PlayLoading)
                    val treePosition by viewModel.position.observeAsState(0)
                    // 由于项目页面和公众号页面只有数据不同，所以使用一个公用的页面
                    ArticleListPage(modifier, tree, treePosition, lazyPagingItems, {
                        viewModel.getDataList()
                    }, {
                        viewModel.searchArticle(it)
                    }, {
                        viewModel.onPositionChanged(it)
                    }) {
                        actions.enterArticle(it)
                    }
                }
                CourseTabs.MINE -> {
                    val viewModel: LoginViewModel = viewModel()
                    val logoutState by viewModel.logoutState.observeAsState(LogoutDefault)
                    ProfilePage(modifier, isLand, actions.toLogin, logoutState, {
                        viewModel.logout()
                    }) {
                        actions.enterArticle(it)
                    }
                }
            }
        }
    }
}

enum class CourseTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectIcon: Int
) {
    HOME_PAGE(R.string.home_page, R.drawable.ic_nav_news_normal, R.drawable.ic_nav_news_actived),
    SYSTEM(
        R.string.home_system,
        R.drawable.ic_nav_discover_normal,
        R.drawable.ic_nav_discover_actived
    ),
    PROJECT(R.string.project, R.drawable.ic_nav_tweet_normal, R.drawable.ic_nav_tweet_actived),
    OFFICIAL_ACCOUNT(
        R.string.official_account,
        R.drawable.ic_nav_discover_normal,
        R.drawable.ic_nav_discover_actived
    ),
    MINE(R.string.mine, R.drawable.ic_nav_my_normal, R.drawable.ic_nav_my_pressed)
}
