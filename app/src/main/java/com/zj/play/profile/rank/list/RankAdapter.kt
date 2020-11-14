package com.zj.play.profile.rank.list

import android.content.Context
import android.view.View
import com.zj.core.view.base.BaseListAdapter
import com.zj.model.model.Rank
import com.zj.play.R
import com.zj.play.profile.share.ShareActivity
import kotlinx.android.synthetic.main.adapter_rank.view.*
import java.util.*

class RankAdapter(
    context: Context,
    rankList: ArrayList<Rank>,
    layoutId: Int = R.layout.adapter_rank
) : BaseListAdapter<Rank>(context, layoutId, rankList) {

    override fun convert(view: View, data: Rank, position: Int) {
        view.rankAdTvUsername.text = data.username
        view.rankAdTvRank.text = mContext.getString(R.string.ranking, data.rank)
        view.rankAdTvCoinCount.text = mContext.getString(R.string.coin, data.coinCount)
        view.rankAdTvLevel.text = mContext.getString(R.string.lever, data.level)
        view.rankAdRlItem.setOnClickListener {
            ShareActivity.actionStart(mContext, false, data.userId)
        }
    }

}
