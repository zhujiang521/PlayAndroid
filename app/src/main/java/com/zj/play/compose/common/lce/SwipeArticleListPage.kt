package com.zj.play.compose.common.lce

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.zj.model.room.entity.Article
import com.zj.play.compose.common.SwipeToRefreshAndLoadLayout
import com.zj.play.compose.common.article.ArticleItem
import com.zj.play.compose.model.*
import com.zj.play.compose.viewmodel.BaseAndroidViewModel
import com.zj.play.compose.viewmodel.REFRESH_START
import com.zj.play.compose.viewmodel.REFRESH_STOP

@Composable
fun SwipeArticleListPage(
    modifier: Modifier = Modifier,
    viewModel: BaseAndroidViewModel<QueryArticle>,
    listState: LazyListState,
    enterArticle: (Article) -> Unit,
    onRefresh: () -> Unit,
    onLoad: () -> Unit,
    onErrorClick: () -> Unit,
    noContent: @Composable () -> Unit
) {

    val refresh by viewModel.refreshState.observeAsState()
    val loadRefresh by viewModel.loadRefreshState.observeAsState()
    val articleData by viewModel.dataLiveData.observeAsState(PlayLoading)
    SwipeToRefreshAndLoadLayout(
        refreshingState = refresh == REFRESH_START,
        loadState = loadRefresh == REFRESH_START,
        onRefresh = onRefresh,
        onLoad = onLoad,
        content = {
            SetLcePage(
                playState = articleData,
                onErrorClick = onErrorClick
            ) {
                viewModel.onLoadRefreshStateChanged(REFRESH_STOP)
                viewModel.onRefreshChanged(REFRESH_STOP)
                val articleList =
                    articleData as PlaySuccess<List<Article>>
                if (articleList.data.isEmpty()) {
                    noContent()
                    return@SetLcePage
                }
                LazyColumn(
                    modifier = modifier,
                    state = listState
                ) {
                    itemsIndexed(articleList.data) { index, article ->
                        ArticleItem(
                            article,
                            index,
                            enterArticle = { urlArgs ->
                                enterArticle(
                                    urlArgs
                                )
                            })
                    }
                }
            }
        })

}