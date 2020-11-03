package com.zj.play.home

import android.content.BroadcastReceiver
import android.util.Log
import com.zj.core.util.LiveDataBus
import com.zj.core.view.base.BaseActivity
import com.zj.play.article.ArticleBroadCast

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/15
 * 描述：文章收藏 BaseActivity，注册文章收藏状态改变的广播
 *
 */
abstract class ArticleCollectBaseActivity : BaseActivity() {

    private var articleReceiver: BroadcastReceiver? = null

    override fun onResume() {
        super.onResume()
        articleReceiver =
            ArticleBroadCast.setArticleChangesReceiver(this) { initData() }
        LiveDataBus.get().getChannel(LOGIN_REFRESH, Boolean::class.java).observe(this, {
            Log.e("ZHUJIANG", "Activity onResume: $it" )
            if (it) initData()
        })
    }

    override fun onPause() {
        super.onPause()
        ArticleBroadCast.clearArticleChangesReceiver(this, articleReceiver)
    }


}