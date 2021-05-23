package com.zj.play.ui.page.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.insets.statusBarsHeight
import com.zj.play.logic.model.*
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.view.ArticleTabRow
import com.zj.play.ui.view.lce.LcePage

/**
 * 由于项目页面和公众号页面只有数据不同，所以写了一个公用的页面
 */
@Composable
fun ArticleListPage(
    modifier: Modifier,
    tree: PlayState,
    position: Int?,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    loadData: () -> Unit,
    searchArticle: (Query) -> Unit,
    onPositionChanged: (Int) -> Unit,
    enterArticle: (ArticleModel) -> Unit,
) {
    val listState = rememberLazyListState()
    var loadState by remember { mutableStateOf(false) }
    var loadPageState by remember { mutableStateOf(false) }
    if (!loadState) {
        loadState = true
        loadData()
    }
    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(modifier = Modifier.statusBarsHeight())
        LcePage(playState = tree, onErrorClick = {
            loadData()
            loadState = true
        }) {
            val data = (tree as PlaySuccess<List<ClassifyModel>>).data
            ArticleTabRow(position, data) { index, id, isFirst ->
                if (!isFirst) {
                    searchArticle(Query(id))
                    onPositionChanged(index)
                } else {
                    if (position == 0 && !loadPageState) {
                        searchArticle(Query(id))
                    }
                }
                loadPageState = true
            }

            ArticleListPaging(modifier, listState, lazyPagingItems, enterArticle)
        }
    }

}