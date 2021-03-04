package com.zj.play.compose.common.article

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zj.play.R
import com.zj.play.compose.common.BookmarkButton
import com.zj.play.compose.common.PlayAppBar
import dev.chrisbanes.accompanist.insets.statusBarsHeight

/**
 * Stateful Article Screen that manages state using [produceUiState]
 *
 * @param postId (state) the post to show
 * @param postsRepository data source for this screen
 * @param onBack (event) request back navigation
 */
@Suppress("DEPRECATION") // allow ViewModelLifecycleScope call
@Composable
fun ArticlePage(
    url: String,
    onBack: () -> Unit
) {
    // Returns a [CoroutineScope] that is scoped to the lifecycle of [ArticleScreen]. When this
    // screen is removed from composition, the scope will be cancelled.
    val coroutineScope = rememberCoroutineScope()
    ArticleScreen(
        url = url,
        onBack = onBack,
        isFavorite = false,
        onToggleFavorite = {}
    )
}

/**
 * Stateless Article Screen that displays a single post.
 *
 * @param url (state) item to display
 * @param onBack (event) request navigate back
 * @param isFavorite (state) is this item currently a favorite
 * @param onToggleFavorite (event) request that this post toggle it's favorite state
 */
@Composable
fun ArticleScreen(
    url: String,
    onBack: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {

    var showDialog by rememberSaveable { mutableStateOf(false) }
    if (showDialog) {
        FunctionalityNotAvailablePopup { showDialog = false }
    }
    val x5WebView = rememberX5WebViewWithLifecycle()
    MaterialTheme {
        Scaffold(
            topBar = {
                PlayAppBar("文章详情", click = {
                    if (x5WebView.canGoBack()) {
                        //返回上个页面
                        x5WebView.goBack()
                    } else {
                        onBack.invoke()
                    }
                })
            },
            content = {
                // Adds view to Compose
                AndroidView(
                    { x5WebView },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 56.dp),
                ) { x5WebView ->
                    // Reading zoom so that AndroidView recomposes when it changes. The getMapAsync lambda
                    // is stored for later, Compose doesn't recognize state reads
                    x5WebView.loadUrl(url)
                }
            },
            bottomBar = {
                BottomBar(
                    post = url,
                    onUnimplementedAction = { showDialog = true },
                    isFavorite = isFavorite,
                    onToggleFavorite = onToggleFavorite
                )
            }
        )
    }
}

/**
 * Bottom bar for Article screen
 *
 * @param post (state) used in share sheet to share the post
 * @param onUnimplementedAction (event) called when the user performs an unimplemented action
 * @param isFavorite (state) if this post is currently a favorite
 * @param onToggleFavorite (event) request this post toggle it's favorite status
 */
@Composable
private fun BottomBar(
    post: String,
    onUnimplementedAction: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    Surface(elevation = 2.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            IconButton(onClick = onUnimplementedAction) {
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "收藏"
                )
            }
            BookmarkButton(
                isBookmarked = isFavorite,
                onClick = onToggleFavorite
            )
            val context = LocalContext.current
            IconButton(onClick = { sharePost(post, context) }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "分享"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onUnimplementedAction) {
                Icon(
                    painter = painterResource(R.drawable.ic_text_settings),
                    contentDescription = "字体设置"
                )
            }
        }
    }
}

/**
 * Display a popup explaining functionality not available.
 *
 * @param onDismiss (event) request the popup be dismissed
 */
@Composable
private fun FunctionalityNotAvailablePopup(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "Functionality not available \uD83D\uDE48",
                style = MaterialTheme.typography.body2
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "CLOSE")
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
private fun sharePost(post: String, context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, post)
    }
    context.startActivity(Intent.createChooser(intent, "Share post"))
}