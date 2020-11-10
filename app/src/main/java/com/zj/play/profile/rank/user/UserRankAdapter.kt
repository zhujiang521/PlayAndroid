package com.zj.play.profile.rank.user

import android.content.Context
import android.widget.TextView
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.play.R
import com.zj.model.model.Ranks
import kotlinx.android.synthetic.main.adapter_rank.view.*
import kotlin.collections.ArrayList

class UserRankAdapter(context: Context, layoutId: Int, rankList: ArrayList<Ranks>) :
    CommonAdapter<Ranks>(context, layoutId, rankList) {
    override fun convert(holder: ViewHolder, t: Ranks, position: Int) {
        holder.itemView.rankAdTvUsername.text = t.userName
        holder.itemView.rankAdTvRank.text = mContext.getString(R.string.add_reason, t.reason)
        holder.itemView.rankAdTvCoinCount.text = mContext.getString(R.string.rank, t.coinCount)
        holder.itemView.rankAdTvLevel.text = t.desc
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
