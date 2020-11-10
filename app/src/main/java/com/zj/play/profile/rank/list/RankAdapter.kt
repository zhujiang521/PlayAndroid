package com.zj.play.profile.rank.list

import android.content.Context
import android.widget.RelativeLayout
import android.widget.TextView
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.play.R
import com.zj.model.model.Rank
import com.zj.play.profile.share.ShareActivity
import kotlinx.android.synthetic.main.adapter_rank.view.*
import java.util.*

class RankAdapter(
    context: Context,
    rankList: ArrayList<Rank>,
    layoutId: Int = R.layout.adapter_rank
) :
    CommonAdapter<Rank>(context, layoutId, rankList) {
    override fun convert(holder: ViewHolder, t: Rank, position: Int) {
        holder.itemView.rankAdTvUsername.text = t.username
        holder.itemView.rankAdTvRank.text = mContext.getString(R.string.ranking, t.rank)
        holder.itemView.rankAdTvCoinCount.text = mContext.getString(R.string.coin, t.coinCount)
        holder.itemView.rankAdTvLevel.text = mContext.getString(R.string.lever, t.level)
        holder.itemView.rankAdRlItem.setOnClickListener {
            ShareActivity.actionStart(mContext, false, t.userId)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
