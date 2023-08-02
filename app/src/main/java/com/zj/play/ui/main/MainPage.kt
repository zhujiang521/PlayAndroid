package com.zj.play.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.zj.play.isLand
import com.zj.play.ui.main.nav.CourseTabs
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.page.home.HomePage
import com.zj.play.ui.page.mine.ProfilePage
import com.zj.play.ui.page.official.OfficialAndroidViewModel
import com.zj.play.ui.page.project.ArticleListPage
import com.zj.play.ui.page.project.ProjectAndroidViewModel
import com.zj.play.ui.page.system.SystemPage
import com.zj.play.ui.page.system.SystemViewModel
import com.zj.play.ui.theme.getCurrentColors
import com.zj.play.ui.view.LandNavigation
import com.zj.play.ui.view.LandNavigationItem
import com.zj.utils.XLog
import java.util.Locale

@Composable
fun MainPage(actions: PlayActions) {
    val viewModel: HomeViewModel = hiltViewModel()
    val position by viewModel.position
    MainPageContent(viewModel, actions, position) { tab ->
        viewModel.onPositionChanged(tab)
    }
}

@Composable
fun MainPageContent(
    homeViewModel: HomeViewModel,
    actions: PlayActions, position: CourseTabs?,
    onPositionChanged: (CourseTabs) -> Unit
) {
    val tabs = CourseTabs.values()
    // 当前是否为横屏
    val isLand = isLand()
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            if (!isLand) {
                PlayBottomNavigation(tabs, position, onPositionChanged)
            }
        }
    ) { innerPadding ->
        if (isLand) {
            Row(modifier = Modifier.fillMaxSize()) {
                PlayLandNavigation(tabs, position, onPositionChanged)
                TabRows(innerPadding, position, homeViewModel, actions)
            }
        } else {
            TabRows(innerPadding, position, homeViewModel, actions)
        }
    }
}

@Composable
private fun PlayLandNavigation(
    tabs: Array<CourseTabs>,
    position: CourseTabs?,
    onPositionChanged: (CourseTabs) -> Unit
) {
    LandNavigation {
        val reversalColor = reversalColor()
        tabs.forEach { tab ->
            val color =
                if (tab == position) reversalColor else MaterialTheme.colors.secondary
            LandNavigationItem(
                modifier = Modifier.background(MaterialTheme.colors.primary),
                icon = {
                    val painter: Painter = if (tab == position) {
                        painterResource(tab.selectIcon)
                    } else {
                        painterResource(tab.icon)
                    }
                    Icon(painter, contentDescription = null, tint = color)
                },
                label = {
                    Text(
                        stringResource(tab.title).uppercase(Locale.ROOT),
                        color = color
                    )
                },
                selected = tab == position,
                onClick = {
                    onPositionChanged(tab)
                },
                alwaysShowLabel = true,
            )
        }
    }
}

@Composable
private fun PlayBottomNavigation(
    tabs: Array<CourseTabs>,
    position: CourseTabs?,
    onPositionChanged: (CourseTabs) -> Unit
) {
    BottomNavigation {
        val reversalColor = reversalColor()
        tabs.forEach { tab ->
            val color =
                if (tab == position) reversalColor else MaterialTheme.colors.secondary
            BottomNavigationItem(
                modifier = Modifier.background(MaterialTheme.colors.primary),
                icon = {
                    val painter: Painter = if (tab == position) {
                        painterResource(tab.selectIcon)
                    } else {
                        painterResource(tab.icon)
                    }
                    Icon(painter, contentDescription = null, tint = color)
                },
                label = {
                    Text(
                        stringResource(tab.title).uppercase(Locale.ROOT),
                        color = color
                    )
                },
                selected = tab == position,
                onClick = {
                    onPositionChanged(tab)
                },
                alwaysShowLabel = true,
            )
        }
    }
}

@Composable
private fun TabRows(
    innerPadding: PaddingValues,
    position: CourseTabs?,
    homeViewModel: HomeViewModel,
    actions: PlayActions
) {
    val modifier = Modifier.padding(innerPadding)
    // 当前是否为横屏
    val isLand = isLand()
    when (position) {
        CourseTabs.HOME_PAGE -> {
            // 主页
            homeViewModel.getBanner()
            homeViewModel.getHomeArticle()
            HomePage(modifier, isLand, homeViewModel, actions)
        }

        CourseTabs.SYSTEM -> {
            // 体系
            val viewModel: SystemViewModel = hiltViewModel()
            viewModel.getAndroidSystem()
            SystemPage(modifier, actions, viewModel)
        }

        CourseTabs.PROJECT, CourseTabs.OFFICIAL_ACCOUNT -> {
            val viewModel = if (position == CourseTabs.PROJECT) {
                hiltViewModel<ProjectAndroidViewModel>()
            } else {
                hiltViewModel<OfficialAndroidViewModel>()
            }
            viewModel.getDataList()
            // 由于项目页面和公众号页面只有数据不同，所以使用一个公用的页面
            ArticleListPage(modifier, actions, viewModel)
        }

        CourseTabs.MINE -> {
            // 我的页面
            ProfilePage(modifier, isLand, actions)
        }

        else -> {
            XLog.e("The page display is faulty")
            throw IllegalAccessException()
        }
    }
}

@Composable
private fun reversalColor(): Color {
    val primaryColor = getCurrentColors().primary
    return Color(
        red = (255f - (primaryColor.red * 255f)) / 255f,
        green = (255f - (primaryColor.green * 255f)) / 255f,
        blue = (255f - (primaryColor.blue * 255f)) / 255f,
    )
}