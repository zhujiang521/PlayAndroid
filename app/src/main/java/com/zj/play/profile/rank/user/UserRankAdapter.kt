package com.zj.play.profile.rank.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zj.core.view.base.BaseRecyclerAdapter
import com.zj.model.model.Ranks
import com.zj.play.R
import com.zj.play.databinding.AdapterRankBinding

class UserRankAdapter(private val context: Context, private val rankList: ArrayList<Ranks>) :
    BaseRecyclerAdapter<AdapterRankBinding>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerHolder<AdapterRankBinding> {
        val binding = AdapterRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseRecyclerHolder(binding)
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    override fun onBaseBindViewHolder(position: Int, binding: AdapterRankBinding) {
        val data = rankList[position]
        binding.apply {
            rankAdTvUsername.text = data.userName
            rankAdTvRank.text = context.getString(R.string.add_reason, data.reason)
            rankAdTvCoinCount.text = context.getString(R.string.rank, data.coinCount)
            rankAdTvLevel.text = data.desc
        }
    }

}
