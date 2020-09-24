package com.zj.play.view.share

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zj.core.Play
import com.zj.play.R
import com.zj.play.model.CoinInfo
import com.zj.play.model.ShareModel
import com.zj.play.view.article.ArticleAdapter
import com.zj.play.view.home.ArticleCollectBaseActivity
import com.zj.play.view.rank.user.UserRankActivity
import com.zj.play.view.share.add.AddShareActivity
import kotlinx.android.synthetic.main.activity_share.*
import kotlin.system.measureTimeMillis

const val IS_MINE = "IS_MINE"
const val USER_ID = "USER_ID"

class ShareActivity : ArticleCollectBaseActivity(), View.OnClickListener {

    private val viewModel by lazy { ViewModelProvider(this).get(ShareViewModel::class.java) }
    private var isMine: Boolean = true
    private var userId: Int = 0
    private lateinit var articleAdapter: ArticleAdapter
    private var page = 1

    override fun getLayoutId(): Int {
        return R.layout.activity_share
    }

    override fun initData() {
        isMine = intent.getBooleanExtra(IS_MINE, true)
        userId = intent.getIntExtra(USER_ID, 0)
        if (!isMine) shareTitleBar.setTitle("作者的分享")
        if (isMine) {
            viewModel.articleLiveData.observe(this, {
                setData(it)
            })
        } else {
            viewModel.articleAndCidLiveData.observe(this, {
                setData(it)
            })
        }
        if (Play.isLogin()) {
            shareTitleBar.setRightText("新增")
            shareTitleBar.setRightTextOnClickListener {
                AddShareActivity.actionStart(this)
            }
        }
        getArticleList()
    }

    private fun setData(it: Result<ShareModel>) {
        if (it.isSuccess) {
            val articleList = it.getOrNull()
            if (articleList != null) {
                loadFinished()
                if (page == 1 && viewModel.articleList.size > 0) {
                    viewModel.articleList.clear()
                }
                setUserInfo(articleList.coinInfo)
                viewModel.articleList.addAll(articleList.shareArticles.datas)
                if (viewModel.articleList.size == 0) {
                    showNoContentView("没有数据")
                }
                articleAdapter.notifyDataSetChanged()
            } else {
                showLoadErrorView()
            }
        } else {
            showBadNetworkView { getArticleList() }
        }
    }

    private fun setUserInfo(coinInfo: CoinInfo) {
        shareHeadLl.visibility = View.VISIBLE
        shareTvName.text = coinInfo.username
        shareTvRank.text = "等级 ${coinInfo.level}  排名 ${coinInfo.rank}  积分 ${coinInfo.coinCount}"
    }

    override fun initView() {
        shareTvRank.setOnClickListener(this)
        shareRecycleView.layoutManager = LinearLayoutManager(this)
        articleAdapter = ArticleAdapter(
            this,
            R.layout.adapter_article,
            viewModel.articleList
        )
        articleAdapter.setHasStableIds(true)
        shareRecycleView.adapter = articleAdapter
        shareSmartRefreshLayout.apply {
            setOnRefreshListener { reLayout ->
                reLayout.finishRefresh(measureTimeMillis {
                    page = 1
                    getArticleList()
                }.toInt())
            }
            setOnLoadMoreListener { reLayout ->
                val time = measureTimeMillis {
                    page++
                    getArticleList()
                }.toInt()
                reLayout.finishLoadMore(if (time > 1000) time else 1000)
            }
        }
    }

    private fun getArticleList() {
        if (viewModel.articleList.size <= 0) startLoading()
        if (isMine) {
            viewModel.getArticleList(page)
        } else {
            viewModel.getArticleList(userId, page)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.shareTvRank -> {
                UserRankActivity.actionStart(this)
            }
        }
    }

    companion object {
        fun actionStart(context: Context, isMine: Boolean, userId: Int = 0) {
            val intent = Intent(context, ShareActivity::class.java)
            intent.putExtra(IS_MINE, isMine)
            intent.putExtra(USER_ID, userId)
            context.startActivity(intent)
        }
    }

}