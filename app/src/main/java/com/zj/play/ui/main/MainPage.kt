package com.zj.play.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.zj.utils.XLog

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