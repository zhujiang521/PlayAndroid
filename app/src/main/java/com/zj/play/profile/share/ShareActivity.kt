package com.zj.play.profile.share

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.zj.core.Play
import com.zj.model.model.CoinInfo
import com.zj.model.model.ShareModel
import com.zj.play.R
import com.zj.play.article.ArticleAdapter
import com.zj.play.home.ArticleCollectBaseActivity
import com.zj.play.profile.rank.user.UserRankActivity
import com.zj.play.profile.share.add.AddShareActivity
import kotlinx.android.synthetic.main.activity_share.*

const val IS_MINE = "IS_MINE"
const val USER_ID = "USER_ID"

class ShareActivity : ArticleCollectBaseActivity(), View.OnClickListener {

    private val viewModel by lazy { ViewModelProvider(this).get(ShareViewModel::class.java) }
    private var isMine: Boolean = true
    private var userId: Int = 0
    private lateinit var articleAdapter: ArticleAdapter
    private var page = 1

    override fun getLayoutId(): Int = R.layout.activity_share

    override fun initData() {
        isMine = intent.getBooleanExtra(IS_MINE, true)
        userId = intent.getIntExtra(USER_ID, 0)
        if (!isMine) shareTitleBar.setTitle(getString(R.string.author_share))
        if (isMine) {
            setDataStatus(viewModel.articleLiveData) {
                setArticleData(it)
            }
        } else {
            setDataStatus(viewModel.articleAndCidLiveData) {
                setArticleData(it)
            }
        }
        if (Play.isLogin) {
            shareTitleBar.setRightText(getString(R.string.add))
            shareTitleBar.setRightTextOnClickListener {
                AddShareActivity.actionStart(this)
            }
        }
        getArticleList()
    }

    private fun setArticleData(shareModel: ShareModel) {
        if (page == 1 && viewModel.articleList.size > 0) {
            viewModel.articleList.clear()
        }
        setUserInfo(shareModel.coinInfo)
        viewModel.articleList.addAll(shareModel.shareArticles.datas)
        if (viewModel.articleList.size == 0) {
            showNoContentView(getString(R.string.no_data))
        }
        articleAdapter.notifyDataSetChanged()
    }

    private fun setUserInfo(coinInfo: CoinInfo) {
        shareHeadLl.visibility = View.VISIBLE
        shareTvName.text = coinInfo.username
        shareTvRank.text =
            getString(R.string.man_info, coinInfo.level, coinInfo.rank, coinInfo.coinCount)
    }

    override fun initView() {
        shareTvRank.setOnClickListener(this)
        shareToTopRecyclerView.setRecyclerViewLayoutManager(true)
        articleAdapter = ArticleAdapter(
            this,
            viewModel.articleList
        )
        shareToTopRecyclerView.setAdapter(articleAdapter)
        shareToTopRecyclerView.onRefreshListener({
            page = 1
            getArticleList()
        }, {
            page++
            getArticleList()
        })
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