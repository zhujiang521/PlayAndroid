package com.zj.play.compose.common.lce

import android.util.Log
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
import com.zj.play.compose.viewmodel.BaseViewModel
import com.zj.play.compose.viewmodel.REFRESH_START
import com.zj.play.compose.viewmodel.REFRESH_STOP

private const val TAG = "SwipeArticleListPage"

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
    BaseSwipeListPage(
        viewModel = viewModel,
        onRefresh = onRefresh,
        onLoad = onLoad,
        onErrorClick = onErrorClick
    ) {
        if (it.isEmpty()) {
            noContent()
            return@BaseSwipeListPage
        }
        LazyColumn(
            modifier = modifier,
            state = listState
        ) {
            Log.e(TAG, "SwipeArticleListPage: articleList.data:${it.size}")
            itemsIndexed(it) { index, article ->
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
}

@Composable
fun BaseSwipeListPage(
    viewModel: BaseViewModel,
    onRefresh: () -> Unit,
    onLoad: () -> Unit,
    onErrorClick: () -> Unit,
    onSuccessContent: @Composable (articleList: List<Article>) -> Unit
) {
    val refresh by viewModel.refreshState.observeAsState()
    val loadRefresh by viewModel.loadRefreshState.observeAsState()
    val articleData by viewModel.dataLiveData.observeAsState(PlayLoading)
    Log.e(TAG, "BaseSwipeListPage: articleData:${articleData.javaClass}")
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
                val articleList = (articleData as PlaySuccess<List<Article>>).data
                Log.e(TAG, "BaseSwipeListPage: ${articleList.size}")
                viewModel.onLoadRefreshStateChanged(REFRESH_STOP)
                viewModel.onRefreshChanged(REFRESH_STOP)
                onSuccessContent(articleList)
            }
        })
}
