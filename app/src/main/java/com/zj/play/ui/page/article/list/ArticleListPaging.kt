package com.zj.play.ui.page.article.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.utils.showToast
import com.zj.play.ui.view.lce.ErrorContent
import com.zj.play.ui.view.lce.LoadingContent

@Composable
fun ArticleListPaging(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    enterArticle: (ArticleModel) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = lazyPagingItems
        ) { article ->
            ArticleItem(article) { urlArgs ->
                enterArticle(urlArgs)
            }
        }
        val loadStates = lazyPagingItems.loadState
        when {
            loadStates.refresh is LoadState.Loading -> {
                item { LoadingContent(modifier = Modifier.fillParentMaxSize()) }
            }
            loadStates.append is LoadState.Loading -> {
                item { LoadingContent() }
            }
            loadStates.refresh is LoadState.Error -> {
                val e = lazyPagingItems.loadState.refresh as LoadState.Error
                showToast(e.error.localizedMessage ?: "")
                item {
                    ErrorContent(modifier = Modifier.fillParentMaxSize()) {
                        lazyPagingItems.retry()
                    }
                }
            }
            loadStates.append is LoadState.Error -> {
                val e = lazyPagingItems.loadState.append as LoadState.Error
                showToast(e.error.localizedMessage ?: "")
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
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
