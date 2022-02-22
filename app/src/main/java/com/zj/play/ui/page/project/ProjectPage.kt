package com.zj.play.ui.page.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.model.ClassifyModel
import com.zj.play.logic.model.PlayState
import com.zj.play.logic.model.Query
import com.zj.play.logic.utils.getHtmlText
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.view.lce.LcePage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 由于项目页面和公众号页面只有数据不同，所以写了一个公用的页面
 */
@OptIn(ExperimentalPagerApi::class, kotlinx.coroutines.InternalCoroutinesApi::class)
@Composable
fun ArticleListPage(
    modifier: Modifier,
    tree: PlayState<List<ClassifyModel>>,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    loadData: () -> Unit,
    searchArticle: (Query) -> Unit,
    enterArticle: (ArticleModel) -> Unit,
) {
    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(modifier = Modifier.statusBarsHeight())
        LcePage(playState = tree, onErrorClick = {
            loadData()
        }) { data ->
            val coroutineScope = rememberCoroutineScope()

            // Remember a PagerState
            val pagerState = rememberPagerState()

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
                count = data.size,
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                ArticleListPaging(modifier, lazyPagingItems, enterArticle)
            }

        }
    }

}