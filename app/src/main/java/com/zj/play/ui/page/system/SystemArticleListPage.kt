package com.zj.play.ui.page.system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.zj.play.logic.model.ArticleModel
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.view.PlayAppBar
import com.zj.play.ui.view.lce.NoContent

@Composable
fun SystemArticleListPage(
    name: String?,
    back: () -> Unit,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    enterArticle: (ArticleModel) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PlayAppBar(name ?: "体系文章", click = {
            back()
        })
        if (lazyPagingItems.itemCount > 0) {
            ArticleListPaging(
                Modifier.fillMaxSize(),
                lazyPagingItems,
                enterArticle = enterArticle
            )
        } else {
            NoContent()
        }
    }
}
