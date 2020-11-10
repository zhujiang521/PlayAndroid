package com.zj.play.profile.rank.user

import android.content.Context
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.core.view.custom.BaseCommonAdapter
import com.zj.model.model.Ranks
import com.zj.play.R
import kotlinx.android.synthetic.main.adapter_rank.view.*

class UserRankAdapter(context: Context, layoutId: Int, rankList: ArrayList<Ranks>) :
    BaseCommonAdapter<Ranks>(context, layoutId, rankList) {

    override fun convert(holder: ViewHolder, t: Ranks, position: Int) {
        holder.itemView.rankAdTvUsername.text = t.userName
        holder.itemView.rankAdTvRank.text = mContext.getString(R.string.add_reason, t.reason)
        holder.itemView.rankAdTvCoinCount.text = mContext.getString(R.string.rank, t.coinCount)
        holder.itemView.rankAdTvLevel.text = t.desc
    }

}
