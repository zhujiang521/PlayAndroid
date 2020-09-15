package com.zj.play.view.article

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.zj.core.Play
import com.zj.core.util.showToast
import com.zj.play.network.CollectRepository
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

    fun setCollect(collect: Boolean, id: Int, context: Context) {
        if (Play.isLogin()) {
            collect(collect, id, context)
        } else {
            showToast("当前未登录")
        }
    }

    fun collect(collect: Boolean, id: Int, context: Context) {
        GlobalScope.launch {
            if (collect) {
                val cancelCollects = CollectRepository.cancelCollects(id)
                if (cancelCollects.errorCode == 0) {
                    showToast("取消收藏成功")
                    ArticleBroadCast.sendArticleChangesReceiver(context)
                } else {
                    showToast("取消收藏失败")
                }
            } else {
                val toCollects = CollectRepository.toCollects(id)
                if (toCollects.errorCode == 0) {
                    showToast("收藏成功")
                    ArticleBroadCast.sendArticleChangesReceiver(context)
                } else {
                    showToast("收藏失败")
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