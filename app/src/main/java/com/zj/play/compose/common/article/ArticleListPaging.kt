package com.zj.play.compose.common.article

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.zj.model.room.entity.Article
import com.zj.play.compose.common.lce.ErrorContent
import com.zj.play.compose.common.lce.LoadingContent

private const val TAG = "ArticleListPaging"

@ExperimentalPagingApi
@Composable
fun ArticleListPaging(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    lazyPagingItems: LazyPagingItems<Article>,
    enterArticle: (Article) -> Unit
) {
    Log.e(TAG, "ArticleListPaging: lazyPagingItems.itemCount:${lazyPagingItems.itemCount}")

    when (lazyPagingItems.loadState.refresh) {
        is LoadState.NotLoading -> {
            LazyColumn(
                modifier = modifier,
                state = listState
            ) {
                itemsIndexed(lazyPagingItems) { index, article ->
                    key(article?.id ?: index) {
                        ArticleItem(article, index, enterArticle = { urlArgs ->
                            enterArticle(
                                urlArgs
                            )
                        })
                    }
                }

                when (lazyPagingItems.loadState.append) {
                    is LoadState.NotLoading -> itemsIndexed(lazyPagingItems) { index, article ->
                        key(article?.id ?: index) {
                            ArticleItem(article, index, enterArticle = { urlArgs ->
                                enterArticle(
                                    urlArgs
                                )
                            })
                        }
                    }
                    LoadState.Loading -> item {
                        LoadingContent()
                    }
                    is LoadState.Error ->
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
        LoadState.Loading -> LoadingContent()
        is LoadState.Error -> ErrorContent {
            lazyPagingItems.retry()
        }
    }
}
