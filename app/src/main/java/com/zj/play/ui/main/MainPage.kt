package com.zj.play.ui.main

import android.content.res.Configuration
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.zj.play.logic.utils.XLog
import com.zj.play.ui.main.nav.CourseTabs
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.page.home.HomePage
import com.zj.play.ui.page.mine.ProfilePage
import com.zj.play.ui.page.official.OfficialAndroidViewModel
import com.zj.play.ui.page.project.ArticleListPage
import com.zj.play.ui.page.project.ProjectAndroidViewModel
import com.zj.play.ui.page.system.SystemPage
import com.zj.play.ui.page.system.SystemViewModel
import java.util.*

@Composable
fun MainPage(actions: PlayActions) {
    val viewModel: HomeViewModel = hiltViewModel()
    val position by viewModel.position.observeAsState()
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
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        bottomBar = {
            BottomNavigation {
                tabs.forEach { tab ->
                    BottomNavigationItem(
                        modifier = Modifier.background(MaterialTheme.colors.primary),
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
        when (position) {
            CourseTabs.HOME_PAGE -> {
                // 主页
                homeViewModel.getBanner()
                homeViewModel.getHomeArticle()
                HomePage(modifier, isLand, homeViewModel, actions)
            }
            CourseTabs.SYSTEM -> {
                // 体系
                XLog.e("SystemPage 测试 one")
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
                XLog.e("页面显示有问题")
            }
        }
    }
}