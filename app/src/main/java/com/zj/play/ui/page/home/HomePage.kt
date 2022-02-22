package com.zj.play.ui.page.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.zj.banner.BannerPager
import com.zj.play.R
import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.model.BannerBean
import com.zj.play.logic.model.PlayState
import com.zj.play.ui.page.article.list.ArticleListPaging
import com.zj.play.ui.view.PlayAppBar
import com.zj.play.ui.view.lce.LcePage

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    isLand: Boolean = false,
    bannerData: PlayState<List<BannerBean>>,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    loadData: () -> Unit,
    toSearch: () -> Unit,
    toArticleDetails: (ArticleModel) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        PlayAppBar(
            stringResource(id = R.string.home_page),
            showBack = false,
            rightImg = Icons.Rounded.Search,
            showRight = true,
            rightClick = toSearch
        )
        LcePage(
            playState = bannerData,
            onErrorClick = {
                loadData()
            }
        ) { data ->
            if (isLand) {
                Row(modifier = Modifier.fillMaxSize()) {
                    val bannerModifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(vertical = 15.dp)
                    val articleModifier = Modifier.weight(1.5f)
                    HomeContent(
                        data,
                        bannerModifier,
                        articleModifier,
                        lazyPagingItems,
                        toArticleDetails
                    )
                }
            } else {
                HomeContent(
                    data,
                    lazyPagingItems = lazyPagingItems,
                    toArticleDetails = toArticleDetails
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    data: List<BannerBean>,
    modifier: Modifier = Modifier,
    articleModifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<ArticleModel>,
    toArticleDetails: (ArticleModel) -> Unit
) {
    BannerPager(
        items = data,
        modifier = modifier,
        indicatorGravity = Alignment.BottomEnd
    ) {
        toArticleDetails(
            ArticleModel(
                title = it.title,
                link = it.url
            )
        )
    }
    ArticleListPaging(
        articleModifier,
        lazyPagingItems,
        toArticleDetails
    )
}
