package com.zj.play.profile.rank.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zj.core.view.base.BaseRecyclerAdapter
import com.zj.model.model.Rank
import com.zj.play.R
import com.zj.play.databinding.AdapterRankBinding
import com.zj.play.profile.share.ShareActivity

class RankAdapter(
    private val mContext: Context,
    private val rankList: ArrayList<Rank>,
) : BaseRecyclerAdapter<AdapterRankBinding>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerHolder<AdapterRankBinding> {
        val binding = AdapterRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseRecyclerHolder(binding)
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    override fun onBaseBindViewHolder(position: Int, binding: AdapterRankBinding) {
        val data = rankList[position]
        binding.apply {
            rankAdTvUsername.text = data.username
            rankAdTvRank.text = mContext.getString(R.string.ranking, data.rank)
            rankAdTvCoinCount.text = mContext.getString(R.string.coin, data.coinCount)
            rankAdTvTime.text = mContext.getString(R.string.lever, data.level)
            rankAdRlItem.setOnClickListener {
                ShareActivity.actionStart(mContext, false, data.userId)
            }
        }
    }

}