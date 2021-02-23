package com.zj.play.profile.rank.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zj.model.model.Ranks
import com.zj.play.R
import com.zj.play.databinding.AdapterRankBinding

class UserRankAdapter(private val context: Context, private val rankList: ArrayList<Ranks>) :
    RecyclerView.Adapter<UserRankAdapter.ViewHolder>() {

    inner class ViewHolder(binding: AdapterRankBinding) : RecyclerView.ViewHolder(binding.root) {
        val rankAdTvUsername: TextView = binding.rankAdTvUsername
        val rankAdTvRank: TextView = binding.rankAdTvRank
        val rankAdTvCoinCount: TextView = binding.rankAdTvCoinCount
        val rankAdTvLevel: TextView = binding.rankAdTvLevel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRankAdapter.ViewHolder {
        val binding = AdapterRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserRankAdapter.ViewHolder, position: Int) {
        val data = rankList[position]
        with(holder) {
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
