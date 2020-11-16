package com.zj.play.profile.rank.list

import android.content.Context
import com.zj.core.view.base.BaseListAdapter
import com.zj.model.model.Rank
import com.zj.play.R
import com.zj.play.profile.share.ShareActivity
import kotlinx.android.synthetic.main.adapter_rank.*
import java.util.*

class RankAdapter(
    context: Context,
    rankList: ArrayList<Rank>,
    layoutId: Int = R.layout.adapter_rank
) : BaseListAdapter<Rank>(context, layoutId, rankList) {

    override fun convert(holder: ViewHolder, data: Rank, position: Int) {
        with(holder) {
            rankAdTvUsername.text = data.username
            rankAdTvRank.text = mContext.getString(R.string.ranking, data.rank)
            rankAdTvCoinCount.text = mContext.getString(R.string.coin, data.coinCount)
            rankAdTvLevel.text = mContext.getString(R.string.lever, data.level)
            rankAdRlItem.setOnClickListener {
                ShareActivity.actionStart(mContext, false, data.userId)
            }
        }
    }

}
