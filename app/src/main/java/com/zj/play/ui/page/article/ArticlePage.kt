package com.zj.play.ui.page.article

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.web.*
import com.zj.model.ArticleModel
import com.zj.play.R
import com.zj.play.ui.view.bar.PlayAppBar
import com.zj.utils.XLog
import com.zj.utils.getHtmlText
import com.zj.utils.showToast


/**
 * Stateless Article Screen that displays a single post.
 *
 * @param article article
 * @param onBack (event) request navigate back
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArticlePage(
    article: ArticleModel?,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val url = article?.link ?: "https://www.baidu.com"
    val state = rememberWebViewState(url = url)
    val client = object : AccompanistWebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            try {
                if (!url.startsWith("http:") || !url.startsWith("https:")) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(url)
                    )
                    view?.context?.startActivity(intent)
                    return true
                }
            } catch (e: Exception) {
                XLog.e("AccompanistWebViewClient:$e")
                showToast(context, e.message)
                return false
            }
            view?.loadUrl(url)
            return true
        }
    }
    val navigator: WebViewNavigator = rememberWebViewNavigator()
    Scaffold(
        topBar = {
            PlayAppBar(
                getHtmlText(article?.title ?: stringResource(R.string.article_details)),
                click = {
                    if (navigator.canGoBack) {
                        navigator.navigateBack()
                    } else {
                        onBack.invoke()
                    }
                },
                showRight = true,
                rightImg = Icons.Filled.Share,
                rightClick = {
                    sharePost(
                        article?.title,
                        article?.link,
                        context
                    )
                })
        },
        content =
        { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            WebView(
                state = state,
                modifier = modifier.fillMaxSize(),
                onCreated = { webView ->
                    webView.settings.javaScriptEnabled = true
                },
                navigator = navigator,
                client = client,
            )
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

@Preview(showBackground = true)
@Composable
fun ArticlePagePreview() {
    ArticlePage(
        ArticleModel(
            title = "Title",
            superChapterName = "Chapter",
            author = "author"
        )
    ) {
        // 回调
    }
}