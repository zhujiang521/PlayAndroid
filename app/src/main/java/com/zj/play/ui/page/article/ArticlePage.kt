package com.zj.play.ui.page.article

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zj.play.R
import com.zj.play.logic.model.ArticleModel
import com.zj.play.logic.utils.getHtmlText
import com.zj.play.logic.utils.showToast
import com.zj.play.ui.view.PlayAppBar
import com.zj.play.ui.view.rememberWebViewWithLifecycle


/**
 * Stateless Article Screen that displays a single post.
 *
 * @param article article
 * @param onBack (event) request navigate back
 */
@Composable
fun ArticlePage(
    article: ArticleModel?,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val webView = rememberWebViewWithLifecycle()
    Scaffold(
        topBar = {
            PlayAppBar(getHtmlText(article?.title ?: "文章详情"), click = {
                if (webView.canGoBack()) {
                    //返回上个页面
                    webView.goBack()
                } else {
                    onBack.invoke()
                }
            }, showRight = true, rightImg = Icons.Filled.Share, rightClick = {
                sharePost(
                    article?.title,
                    article?.link,
                    context
                )
            })
        },
        content = {
            AndroidView(
                factory = { webView },
                modifier = Modifier
                    .fillMaxSize()
            ) { view ->
                view.loadUrl(article?.link ?: "")
            }
        }
    )
}

/**
 * Show a share sheet for a post
 *
 * @param post to share
 * @param context Android context to show the share sheet in
 */
private fun sharePost(title: String?, post: String?, context: Context) {
    if (title == null || post == null) {
        return
    }
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, title)
        putExtra(Intent.EXTRA_TEXT, post)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.share_article)
        )
    )
}