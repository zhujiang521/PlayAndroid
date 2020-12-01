package com.zj.play.base

import android.content.res.Configuration
import com.zj.play.R
import com.zj.play.home.ArticleCollectBaseActivity
import kotlinx.android.synthetic.main.activity_base_list.*

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/10/20
 * 描述：PlayAndroid
 *
 */
abstract class BaseListActivity : ArticleCollectBaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_base_list

    protected var page = 1

    override fun initData() {
        getDataList()
    }

    abstract fun getDataList()

    override fun initView() {
        baseListToTop.setRecyclerViewLayoutManager(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        baseListToTop.onRefreshListener({
            page = 1
            getDataList()
        }, {
            page++
            getDataList()
        })
    }

    abstract fun isStaggeredGrid(): Boolean

}
