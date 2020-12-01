package com.zj.play.base

import android.content.res.Configuration
import com.zj.play.R
import com.zj.play.article.ArticleAdapter
import com.zj.play.home.ArticleCollectBaseFragment
import kotlinx.android.synthetic.main.fragment_base_list.*

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/10/20
 * 描述：PlayAndroid
 *
 */
abstract class BaseListFragment : ArticleCollectBaseFragment() {

    protected lateinit var articleAdapter: ArticleAdapter
    protected var page = 1

    override fun getLayoutId(): Int = R.layout.fragment_base_list

    override fun initView() {
        baseFragmentToTop.setRecyclerViewLayoutManager(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        baseFragmentToTop.setAdapter(articleAdapter)
        baseFragmentToTop.onRefreshListener({
            page = 1
            refreshData()
        }, {
            page++
            refreshData()
        })
    }

}
