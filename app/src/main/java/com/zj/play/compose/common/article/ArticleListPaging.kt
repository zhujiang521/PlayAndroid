package com.zj.play.compose.common.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.zj.core.util.showToast
import com.zj.model.room.entity.Article
import com.zj.play.compose.common.lce.ErrorContent
import com.zj.play.compose.common.lce.LoadingContent

@ExperimentalPagingApi
@Composable
fun ArticleListPaging(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    lazyPagingItems: LazyPagingItems<Article>,
    enterArticle: (Article) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {

        items(lazyPagingItems) { article ->

            ArticleItem(article) { urlArgs ->
                enterArticle(urlArgs)
            }
        }

        val mediator = lazyPagingItems.loadState.mediator
        when {
            mediator?.refresh is LoadState.Loading -> {
                item { LoadingContent(modifier = Modifier.fillParentMaxSize()) }
            }
            mediator?.append is LoadState.Loading -> {
                item { LoadingContent() }
            }
            mediator?.refresh is LoadState.Error -> {
                val e = lazyPagingItems.loadState.refresh as LoadState.Error
                showToast(e.error.localizedMessage ?: "")
                item {
                    ErrorContent(modifier = Modifier.fillParentMaxSize()) {
                        lazyPagingItems.retry()
                    }
                }
            }
            mediator?.append is LoadState.Error -> {
                val e = lazyPagingItems.loadState.append as LoadState.Error
                showToast(e.error.localizedMessage ?: "")
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            onClick = { lazyPagingItems.retry() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}
