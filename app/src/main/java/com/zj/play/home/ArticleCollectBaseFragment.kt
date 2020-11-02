package com.zj.play.home

import android.content.BroadcastReceiver
import android.util.Log
import com.zj.core.util.LiveDataBus
import com.zj.core.view.BaseFragment
import com.zj.play.article.ArticleBroadCast


/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/15
 * 描述：文章收藏 BaseFragment，注册文章收藏状态改变的广播
 *
 */
abstract class ArticleCollectBaseFragment : BaseFragment() {

    private var articleReceiver: BroadcastReceiver? = null

    override fun onResume() {
        super.onResume()
        articleReceiver =
            ArticleBroadCast.setArticleChangesReceiver(activity!!) { refreshData() }
        LiveDataBus.get().getChannel(LOGIN_REFRESH, Boolean::class.java).observe(this, {
            Log.e("ZHUJIANG", "Fragment onResume: $it" )
            if (it) refreshData()
        })
    }

    abstract fun refreshData()

    override fun onPause() {
        super.onPause()
        ArticleBroadCast.clearArticleChangesReceiver(activity!!, articleReceiver)
    }

}

const val LOGIN_REFRESH = "LOGIN_REFRESH"