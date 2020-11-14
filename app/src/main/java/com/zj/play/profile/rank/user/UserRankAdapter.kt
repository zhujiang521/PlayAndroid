package com.zj.play.profile.rank.user

import android.content.Context
import android.view.View
import com.zj.core.view.base.BaseListAdapter
import com.zj.model.model.Ranks
import com.zj.play.R
import kotlinx.android.synthetic.main.adapter_rank.view.*

class UserRankAdapter(context: Context, layoutId: Int, rankList: ArrayList<Ranks>) :
    BaseListAdapter<Ranks>(context, layoutId, rankList) {

    override fun convert(view: View, data: Ranks, position: Int) {
        view.rankAdTvUsername.text = data.userName
        view.rankAdTvRank.text = mContext.getString(R.string.add_reason, data.reason)
        view.rankAdTvCoinCount.text = mContext.getString(R.string.rank, data.coinCount)
        view.rankAdTvLevel.text = data.desc
    }

}
