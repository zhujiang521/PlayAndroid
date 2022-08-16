package com.zj.play.home

import android.content.BroadcastReceiver
import android.os.Bundle
import android.view.View
import com.zj.core.view.base.BaseFragment
import com.zj.play.article.ArticleBroadCast


/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/15
 * 描述：文章收藏 BaseFragment，注册文章收藏状态改变的广播
 *
 */
abstract class ArticleCollectBaseFragment : BaseFragment() {

    private var articleReceiver: BroadcastReceiver? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleReceiver =
            ArticleBroadCast.setArticleChangesReceiver(requireActivity()) { refreshData() }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    abstract fun refreshData()

    override fun onDestroyView() {
        super.onDestroyView()
        ArticleBroadCast.clearArticleChangesReceiver(requireActivity(), articleReceiver)
    }

}