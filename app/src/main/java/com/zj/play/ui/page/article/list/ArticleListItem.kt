package com.zj.play.ui.page.article.list

import android.text.TextUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.zj.banner.utils.ImageLoader
import com.zj.model.ArticleModel
import com.zj.play.R
import com.zj.utils.getHtmlText

@Composable
fun ArticleItem(
    article: ArticleModel?,
    enterArticle: (urlArgs: ArticleModel) -> Unit,
) {
    if (article == null) return
    ArticleListItem(
        article = article,
        onClick = { enterArticle(article) },
        modifier = Modifier
            .height(96.dp)
            .padding(start = 16.dp, top = 8.dp),
        shape = RoundedCornerShape(topStart = 24.dp)
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ArticleListItem(
    article: ArticleModel,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    onClick: () -> Unit,
) {
    Surface(
        elevation = 1.dp,
        shape = shape,
        modifier = modifier
    ) {
        Row(modifier = Modifier.clickable(onClick = onClick)) {
            ImageLoader(
                article.envelopePic.ifBlank { R.drawable.img_default },
                Modifier.aspectRatio(1f)
            )
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
            ) {
                Text(
                    text = getHtmlText(article.title),
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 4.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = article.superChapterName,
                        style = MaterialTheme.typography.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )
                    Text(
                        text = if (TextUtils.isEmpty(article.author)) article.shareUser else article.author,
                        style = MaterialTheme.typography.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleItemPreview() {
    ArticleListItem(
        article = ArticleModel(
            title = "标题",
            superChapterName = "分类",
            author = "作者"
        ),
        modifier = Modifier
            .height(96.dp)
            .padding(start = 16.dp, top = 8.dp),
        shape = RoundedCornerShape(topStart = 24.dp)
    ){
        // 回调
    }
}