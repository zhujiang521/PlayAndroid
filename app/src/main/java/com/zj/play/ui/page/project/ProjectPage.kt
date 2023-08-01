@file:OptIn(ExperimentalFoundationApi::class)

package com.zj.play.ui.page.project

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.zj.banner.utils.pagerTabIndicatorOffset
import com.zj.model.*
import com.zj.play.logic.viewmodel.BaseAndroidViewModel
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.view.lce.LcePage
import com.zj.utils.getHtmlText
import kotlinx.coroutines.launch

/**
 * 由于项目页面和公众号页面只有数据不同，所以写了一个公用的页面
 */
@Composable
fun ArticleListPage(
    modifier: Modifier,
    actions: PlayActions,
    viewModel: BaseAndroidViewModel
) {
    val lazyPagingItems = viewModel.articleResult.collectAsLazyPagingItems()
    val tree by viewModel.treeState
    // 由于项目页面和公众号页面只有数据不同，所以使用一个公用的页面
    ArticleListPageContent(
        modifier, tree, lazyPagingItems,
        loadData = {
            viewModel.getDataList()
        },
        searchArticle = {
            viewModel.searchArticle(it)
        },
    ) {
        actions.enterArticle(it)
    }
}

@Composable
fun ArticleListPageContent(
    modifier: Modifier,
    tree: PlayState<List<ClassifyModel>>,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    loadData: () -> Unit,
    searchArticle: (Query) -> Unit,
    enterArticle: (ArticleModel) -> Unit,
) {
    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        LcePage(playState = tree, onErrorClick = {
            loadData()
        }) { data ->
            val coroutineScope = rememberCoroutineScope()

            // Remember a PagerState
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) {
                data.size
            }

            ScrollableTabRow(
                // Our selected tab is our current page
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.wrapContentWidth(),
                edgePadding = 3.dp,
                backgroundColor = MaterialTheme.colors.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }
            ) {
                // Add tabs for all of our pages
                data.forEachIndexed { index, projectClassify ->
                    Tab(
                        text = { Text(getHtmlText(projectClassify.name)) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            // Animate to the selected page when clicked
                            coroutineScope.launch {
                                searchArticle(Query(data[index].id))
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            LaunchedEffect(pagerState) {
                // Collect from the pager state a snapshotFlow reading the currentPage
                snapshotFlow { pagerState.currentPage }.collect { page ->
                    searchArticle(Query(data[page].id))
                }
            }

            HorizontalPager(
                state = pagerState,
                key = {
                    try {
                        data[it].id
                    } catch (e: Exception) {
                        e.printStackTrace()
                        System.currentTimeMillis()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                ArticleListPaging(modifier, lazyPagingItems, enterArticle)
            }

        }
    }

}