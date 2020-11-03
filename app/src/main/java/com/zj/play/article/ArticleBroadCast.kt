package com.zj.play.article

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/14
 * 描述：PlayAndroid
 *
 */
object ArticleBroadCast {

    const val COLLECT_RECEIVER = "com.zj.play.COLLECT"

    fun sendArticleChangesReceiver(context: Context) {
        val intent = Intent(COLLECT_RECEIVER)
        intent.setPackage(context.packageName)
        context.sendBroadcast(intent)
    }

    fun setArticleChangesReceiver(c: Activity, block: () -> Unit): BroadcastReceiver? {
        val filter = IntentFilter()
        filter.addAction(COLLECT_RECEIVER)
        val r = ArticleBroadcastReceiver(block)
        //LocalBroadcastManager.getInstance(c).registerReceiver(r, filter)
        c.registerReceiver(r, filter)
        return r
    }

    fun clearArticleChangesReceiver(c: Activity, r: BroadcastReceiver?) {
        if (r != null)
            LocalBroadcastManager.getInstance(c).unregisterReceiver(r)
    }

}

private class ArticleBroadcastReceiver(val block: () -> Unit) :
    BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ArticleBroadCast.COLLECT_RECEIVER) {
            block.invoke()
        }
    }
}