package com.zj.play.ui.page.article.list

import android.text.TextUtils
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zj.play.R
import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.utils.getHtmlText
import com.zj.play.ui.view.NetworkImage

@Composable
fun ArticleItem(
    article: ArticleModel?,
    enterArticle: (urlArgs: ArticleModel) -> Unit,
) {
    if (article == null) return
    ArticleListItem(
        article = article,
        onClick = { enterArticle(article) },
        modifier = Modifier.height(96.dp).padding(start = 16.dp, top = 8.dp),
        shape = RoundedCornerShape(topStart = 24.dp)
    )
}

@Composable
fun ArticleListItem(
    article: ArticleModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    elevation: Dp = 1.dp,
    titleStyle: TextStyle = MaterialTheme.typography.subtitle1
) {
    Surface(
        elevation = elevation,
        shape = shape,
        modifier = modifier
    ) {
        Row(modifier = Modifier.clickable(onClick = onClick)) {
            if (article.envelopePic.isNotBlank()) {
                NetworkImage(
                    url = article.envelopePic,
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(1f)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.img_default), contentDescription = null,
                    modifier = Modifier
                        .height(96.dp)
                        .width(91.5.dp)
                )
            }
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
                    style = titleStyle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 4.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = article.superChapterName,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)
                    )
                    Text(
                        text = if (TextUtils.isEmpty(article.author)) article.shareUser else article.author,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.caption,
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