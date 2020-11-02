package com.zj.play.article

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.zj.core.util.showToast
import com.zj.play.R
import com.zj.network.repository.CollectRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/14
 * 描述：PlayAndroid
 *
 */
object ArticleUtils {

    fun collect(collect: Boolean, id: Int, originId: Int, context: Context) {
        GlobalScope.launch {
            if (collect) {
                val cancelCollects =
                    CollectRepository.cancelCollects(if (originId != -1) originId else id)
                if (cancelCollects.errorCode == 0) {
                    showToast(context.getString(R.string.collection_cancelled_successfully))
                    ArticleBroadCast.sendArticleChangesReceiver(context)
                } else {
                    showToast(context.getString(R.string.failed_to_cancel_collection))
                }
            } else {
                val toCollects = CollectRepository.toCollects(id)
                if (toCollects.errorCode == 0) {
                    showToast(context.getString(R.string.collection_successful))
                    ArticleBroadCast.sendArticleChangesReceiver(context)
                } else {
                    showToast(context.getString(R.string.collection_failed))
                }

            }
        }
    }

    fun copyToClipboard(context: Context, text: String?) {
        val systemService: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        systemService.setPrimaryClip(ClipData.newPlainText("text", text))
    }

    fun jumpBrowser(context: Context, url: String) {
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }

    fun shareUrl(context: Context, url: String, name: String) {
        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "text/plain"
        textIntent.putExtra(Intent.EXTRA_TEXT, url)
        context.startActivity(Intent.createChooser(textIntent, name))
    }

}