package com.zj.play.compose.common.article

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.zj.model.room.entity.Article
import com.zj.play.compose.common.lce.LoadingContent

//var keys = 0

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

        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
                LoadingContent()
            }
        }

        itemsIndexed(lazyPagingItems) { index, article ->
            ArticleItem(
                article,
                index,
                enterArticle = { urlArgs ->
                    enterArticle(
                        urlArgs
                    )
                })
        }

//        items(lazyPagingItems.itemCount, key = {
//            keys++
//        }) { index ->
//            val item = lazyPagingItems[index]
//            ArticleItem(
//                item,
//                index,
//                enterArticle = { urlArgs ->
//                    enterArticle(
//                        urlArgs
//                    )
//                })
//        }

        if (lazyPagingItems.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    color = Color.Magenta
                )
            }
        }

    }
}