package com.zj.play.view.rank.list

import android.content.Context
import android.widget.RelativeLayout
import android.widget.TextView
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.play.R
import com.zj.play.model.Rank
import com.zj.play.view.share.ShareActivity
import java.util.*

class RankAdapter(
    context: Context,
    rankList: ArrayList<Rank>,
    layoutId: Int = R.layout.adapter_rank
) :
    CommonAdapter<Rank>(context, layoutId, rankList) {
    override fun convert(holder: ViewHolder, t: Rank, position: Int) {
        val rankAdRlItem = holder.getView<RelativeLayout>(R.id.rankAdRlItem)
        val rankAdTvUsername = holder.getView<TextView>(R.id.rankAdTvUsername)
        val rankAdTvRank = holder.getView<TextView>(R.id.rankAdTvRank)
        val rankAdTvCoinCount = holder.getView<TextView>(R.id.rankAdTvCoinCount)
        val rankAdTvLevel = holder.getView<TextView>(R.id.rankAdTvLevel)
        rankAdTvUsername.text = t.username
        rankAdTvRank.text = "第${t.rank}名"
        rankAdTvCoinCount.text = "${t.coinCount}积分"
        rankAdTvLevel.text = "${t.level}级"
        rankAdRlItem.setOnClickListener {
            ShareActivity.actionStart(mContext, false, t.userId)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
