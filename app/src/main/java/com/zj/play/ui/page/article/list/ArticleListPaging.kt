package com.zj.play.ui.page.article.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.zj.model.ArticleModel
import com.zj.play.ui.view.lce.ErrorContent
import com.zj.play.ui.view.lce.LoadingContent
import com.zj.utils.XLog

@Composable
fun ArticleListPaging(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    enterArticle: (ArticleModel) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            count = lazyPagingItems
                .itemCount,
            key = lazyPagingItems.itemKey(),
            contentType = lazyPagingItems.itemContentType(
            )
        ) { index ->
            val item = lazyPagingItems[index]
            ArticleItem(item) { urlArgs ->
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
                XLog.e("Exception:$e")
                item {
                    ErrorContent(modifier = Modifier.fillParentMaxSize()) {
                        lazyPagingItems.retry()
                    }
                }
            }

            loadStates.append is LoadState.Error -> {
                val e = lazyPagingItems.loadState.append as LoadState.Error
                XLog.e("loadStates two:${e}")
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
