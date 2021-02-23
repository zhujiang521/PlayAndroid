package com.zj.play.base

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zj.play.article.ArticleAdapter
import com.zj.play.databinding.FragmentBaseListBinding
import com.zj.play.home.ArticleCollectBaseFragment

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/10/20
 * 描述：PlayAndroid
 *
 */
abstract class BaseListFragment : ArticleCollectBaseFragment() {

    protected lateinit var binding: FragmentBaseListBinding

    protected lateinit var articleAdapter: ArticleAdapter
    protected var page = 1

    override fun getLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        binding = FragmentBaseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.baseFragmentToTop.setRecyclerViewLayoutManager(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        binding.baseFragmentToTop.setAdapter(articleAdapter)
        binding.baseFragmentToTop.onRefreshListener({
            page = 1
            refreshData()
        }, {
            page++
            refreshData()
        })
    }

}
