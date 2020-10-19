package com.zj.play.view.home

import android.content.BroadcastReceiver
import com.zj.core.view.BaseFragment
import com.zj.play.view.article.ArticleBroadCast

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/15
 * 描述：PlayAndroid
 *
 */
abstract class ArticleCollectBaseFragment : BaseFragment() {

    private var articleReceiver: BroadcastReceiver? = null

    override fun onResume() {
        super.onResume()
        articleReceiver =
            ArticleBroadCast.setArticleChangesReceiver(activity!!) { refreshData() }
    }

    abstract fun refreshData()

    override fun onPause() {
        super.onPause()
        ArticleBroadCast.clearArticleChangesReceiver(activity!!, articleReceiver)
    }

}