package com.zj.play.ui.page.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.model.ClassifyModel
import com.zj.play.logic.model.PlayState
import com.zj.play.logic.model.Query
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.view.ArticleTabRow
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
    val listState = rememberLazyListState()
    var position by rememberSaveable { mutableStateOf(0) }
    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(modifier = Modifier.statusBarsHeight())
        LcePage(playState = tree, onErrorClick = {
            loadData()
        }) { data ->
            val coroutineScope = rememberCoroutineScope()

            val pagerState = rememberPagerState()
            ArticleTabRow(position, data, pagerState) { index, id ->
                searchArticle(Query(id))
                position = index
                coroutineScope.launch {
                    pagerState.scrollToPage(index)
                }
            }
            LaunchedEffect(pagerState) {
                // Collect from the pager state a snapshotFlow reading the currentPage
                snapshotFlow { pagerState.currentPage }.collect {
                    position = it
                    coroutineScope.launch {
                        pagerState.scrollToPage(it)
                    }
                }
            }
            HorizontalPager(
                count = lazyPagingItems.itemCount,
                state = pagerState,
            ) {
                ArticleListPaging(modifier, listState, lazyPagingItems, enterArticle)
            }
        }
    }

}