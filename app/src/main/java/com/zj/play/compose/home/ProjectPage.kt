package com.zj.play.compose.home

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
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.ProjectClassify
import com.zj.play.compose.common.article.ArticleListPaging
import com.zj.play.compose.common.article.ArticleTabRow
import com.zj.play.compose.common.article.ToTopButton
import com.zj.play.compose.common.lce.SetLcePage
import com.zj.play.compose.model.PlayLoading
import com.zj.play.compose.model.PlaySuccess
import com.zj.play.compose.model.Query
import com.zj.play.compose.viewmodel.*
import dev.chrisbanes.accompanist.insets.statusBarsHeight

@ExperimentalPagingApi
@Composable
fun ProjectPage(
    enterArticle: (Article) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProjectViewModel = viewModel(),
    projectViewModel: ProjectListViewModel = viewModel(),
) {
    ArticleListPage(
        modifier = modifier,
        enterArticle = enterArticle,
        viewModel = viewModel,
        articleViewModel = projectViewModel
    )
}

@ExperimentalPagingApi
@Composable
fun ArticleListPage(
    modifier: Modifier,
    enterArticle: (Article) -> Unit,
    viewModel: BaseAndroidViewModel<Boolean>,
    articleViewModel: BaseArticleViewModel
) {
    val listState = rememberLazyListState()
    var loadState by remember { mutableStateOf(false) }
    var loadPageState by remember { mutableStateOf(false) }
    val lazyPagingItems = articleViewModel.articleResult.collectAsLazyPagingItems()
    val refresh by viewModel.refreshState.observeAsState()

    if (!loadState && refresh != REFRESH_START) {
        loadState = true
        viewModel.getDataList(false)
    }
    val tree by viewModel.dataLiveData.observeAsState(PlayLoading)
    val position by viewModel.position.observeAsState()
    Column(modifier = Modifier.background(color = MaterialTheme.colors.primary)) {
        Spacer(modifier = Modifier.statusBarsHeight())
        SetLcePage(playState = tree, onErrorClick = {
            viewModel.getDataList(false)
            loadState = true
        }) {
            val data = (tree as PlaySuccess<List<ProjectClassify>>).data
            ArticleTabRow(position, data) { index, id, isFirst ->
                if (!isFirst) {
                    articleViewModel.searchArticle(Query(id))
                    viewModel.onPositionChanged(index)
                } else {
                    if (position == 0 && !loadPageState) {
                        articleViewModel.searchArticle(Query(id))
                    }
                }
                loadPageState = true
            }

            ArticleListPaging(modifier, listState, lazyPagingItems, enterArticle)
        }
    }

    if (tree is PlaySuccess<*>) {
        ToTopButton(listState)
    }
}