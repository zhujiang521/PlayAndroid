package com.zj.play.profile.rank.user

import android.content.Context
import com.zj.core.view.base.BaseListAdapter
import com.zj.model.model.Ranks
import com.zj.play.R
import kotlinx.android.synthetic.main.adapter_rank.*

class UserRankAdapter(context: Context, layoutId: Int, rankList: ArrayList<Ranks>) :
    BaseListAdapter<Ranks>(context, layoutId, rankList) {

    override fun convert(holder: ViewHolder, data: Ranks, position: Int) {
        with(holder) {
            rankAdTvUsername.text = data.userName
            rankAdTvRank.text = mContext.getString(R.string.add_reason, data.reason)
            rankAdTvCoinCount.text = mContext.getString(R.string.rank, data.coinCount)
            rankAdTvLevel.text = data.desc
        }
    }

}
