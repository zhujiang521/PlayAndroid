package com.zj.play.ui.page.article

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.zj.model.ArticleModel
import com.zj.play.R
import com.zj.play.ui.view.bar.PlayAppBar
import com.zj.play.ui.view.lce.LoadingContent
import com.zj.utils.XLog
import com.zj.utils.getHtmlText


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
        content = { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            WebView(
                state = state,
                modifier = modifier.fillMaxSize(),
                onCreated = { webView ->
                    webView.apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        webViewClient = AndroidWebViewClient()
                    }
                },
                navigator = navigator,
            )
            when (state.loadingState) {
                LoadingState.Finished -> {
                    XLog.i("Load complete")
                }

                else -> {
                    LoadingContent(modifier)
                }
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