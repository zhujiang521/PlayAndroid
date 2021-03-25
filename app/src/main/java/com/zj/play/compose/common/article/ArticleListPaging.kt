package com.zj.play.compose.common.article

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.blankj.utilcode.util.NetworkUtils
import com.zj.core.util.showToast
import com.zj.model.room.entity.Article
import com.zj.play.R
import com.zj.play.compose.common.lce.ErrorContent
import com.zj.play.compose.common.lce.LoadingContent
import kotlinx.coroutines.flow.Flow

private const val TAG = "ArticleListPaging"

@Composable
fun ArticleListPaging(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    flowPagingItems: Flow<PagingData<Article>>,
    enterArticle: (Article) -> Unit
) {

    val lazyPagingItems = flowPagingItems.collectAsLazyPagingItems()

    Log.e(TAG, "ArticleListPaging: lazyPagingItems.itemCount:${lazyPagingItems.itemCount}")
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {

        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
                LoadingContent()
            }
        }
        Log.e(TAG, "ArticleListPaging: lazyPagingItems:${lazyPagingItems.itemCount}")
        lazyPagingItems.loadState.prepend.endOfPaginationReached
        itemsIndexed(lazyPagingItems) { index, article ->
            if (article != null) {
                ArticleItem(
                    article,
                    index,
                    enterArticle = { urlArgs ->
                        enterArticle(
                            urlArgs
                        )
                    })
            } else {
                ErrorContent {
                    lazyPagingItems.retry()
                }
            }
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