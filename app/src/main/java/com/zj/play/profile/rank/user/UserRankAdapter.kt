package com.zj.play.profile.rank.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zj.core.view.base.BaseRecyclerAdapter
import com.zj.model.model.Ranks
import com.zj.play.R
import com.zj.play.databinding.AdapterRankBinding

class UserRankAdapter(private val context: Context, private val rankList: ArrayList<Ranks>) :
    BaseRecyclerAdapter<UserRankAdapter.ViewHolder>() {

    private var binding: AdapterRankBinding? = null

    inner class ViewHolder(binding: AdapterRankBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRankAdapter.ViewHolder {
        binding = AdapterRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: UserRankAdapter.ViewHolder, position: Int) {
        val data = rankList[position]
        binding?.apply {
            rankAdTvUsername.text = data.userName
            rankAdTvRank.text = context.getString(R.string.add_reason, data.reason)
            rankAdTvCoinCount.text = context.getString(R.string.rank, data.coinCount)
            rankAdTvLevel.text = data.desc
        }
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

}
