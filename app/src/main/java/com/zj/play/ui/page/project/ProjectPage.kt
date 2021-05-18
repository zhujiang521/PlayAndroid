package com.zj.play.ui.page.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import com.zj.play.logic.base.viewmodel.BaseViewModel
import com.zj.play.logic.model.*
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.page.home.REFRESH_START
import com.zj.play.ui.view.ArticleTabRow
import com.zj.play.ui.view.lce.LcePage
import dev.chrisbanes.accompanist.insets.statusBarsHeight

@ExperimentalPagingApi
@Composable
fun ProjectPage(
    enterArticle: (ArticleModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProjectViewModel = viewModel(),
) {
    ArticleListPage(
        modifier = modifier,
        enterArticle = enterArticle,
        viewModel = viewModel,
    )
}

@ExperimentalPagingApi
@Composable
fun ArticleListPage(
    modifier: Modifier,
    enterArticle: (ArticleModel) -> Unit,
    viewModel: BaseViewModel,
) {
    val listState = rememberLazyListState()
    var loadState by remember { mutableStateOf(false) }
    var loadPageState by remember { mutableStateOf(false) }
    val lazyPagingItems = viewModel.articleResult.collectAsLazyPagingItems()

    if (!loadState) {
        loadState = true
        viewModel.getDataList()
    }
    val tree by viewModel.treeLiveData.observeAsState(PlayLoading)
    val position by viewModel.position.observeAsState()
    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(modifier = Modifier.statusBarsHeight())
        LcePage(playState = tree, onErrorClick = {
            viewModel.getDataList()
            loadState = true
        }) {
            val data = (tree as PlaySuccess<List<ClassifyModel>>).data
            ArticleTabRow(position, data) { index, id, isFirst ->
                if (!isFirst) {
                    viewModel.searchArticle(Query(id))
                    viewModel.onPositionChanged(index)
                } else {
                    if (position == 0 && !loadPageState) {
                        viewModel.searchArticle(Query(id))
                    }
                }
                loadPageState = true
            }

            ArticleListPaging(modifier, listState, lazyPagingItems, enterArticle)
        }
    }

}