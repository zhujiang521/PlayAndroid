@file:OptIn(ExperimentalLayoutApi::class)

package com.zj.play.ui.page.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.zj.model.ArticleModel
import com.zj.model.HotkeyModel
import com.zj.model.PlayState
import com.zj.model.PlaySuccess
import com.zj.play.R
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.view.StaggeredGrid
import com.zj.play.ui.view.bar.SearchBar
import com.zj.play.ui.view.lce.LcePage
import com.zj.utils.showToast

@Composable
fun SearchPage(actions: PlayActions) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val lazyPagingItems = viewModel.articleResult.collectAsLazyPagingItems()
    val hotkeyModels by viewModel.hotkeyState
    val context = LocalContext.current
    SearchPageContent(back = actions.upPress,
        lazyPagingItems = lazyPagingItems,
        hotkeyModels = hotkeyModels,
        loadHotkey = {
            if (hotkeyModels !is PlaySuccess<*>) {
                viewModel.getHotkeyList()
            }
        },
        enterArticle = {
            actions.enterArticle(it)
        },
        searchArticle = {
            if (it.isEmpty()) {
                showToast(context, R.string.search_content_hint)
                return@SearchPageContent
            }
            viewModel.getSearchArticle(it)
        })
}

@Composable
fun SearchPageContent(
    back: () -> Unit,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    hotkeyModels: PlayState<List<HotkeyModel>>,
    loadHotkey: () -> Unit,
    enterArticle: (ArticleModel) -> Unit,
    searchArticle: (String) -> Unit,
) {
    var loadArticleState by rememberSaveable { mutableStateOf(false) }
    if (!loadArticleState) {
        loadArticleState = true
        loadHotkey()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary)
            .imeNestedScroll()
    ) {
        SearchBar(back, searchArticle)
        if (lazyPagingItems.itemCount > 0) {
            ArticleListPaging(
                Modifier.fillMaxSize(),
                lazyPagingItems,
                enterArticle
            )
        } else {
            LcePage(
                playState = hotkeyModels,
                onErrorClick = loadHotkey
            ) {
                loadArticleState = true
                StaggeredGrid {
                    it.forEach {
                        Chip(text = it.name,
                            modifier = Modifier
                                .height(50.dp)
                                .padding(8.dp)
                                .clickable {
                                    searchArticle(it.name)
                                }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_head),
                contentDescription = "",
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .shadow(1.dp, RoundedCornerShape(3))
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Preview(name = "搜索头", widthDp = 360, heightDp = 70, showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar({}, {})
}
