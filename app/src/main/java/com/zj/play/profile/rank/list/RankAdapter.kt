package com.zj.play.profile.rank.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zj.model.model.Rank
import com.zj.play.R
import com.zj.play.databinding.AdapterRankBinding
import com.zj.play.profile.share.ShareActivity
import java.util.*

class RankAdapter(
    private val mContext: Context,
    private val rankList: ArrayList<Rank>,
) : RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    inner class ViewHolder(binding: AdapterRankBinding) : RecyclerView.ViewHolder(binding.root)

    private var binding: AdapterRankBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankAdapter.ViewHolder {
        binding = AdapterRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: RankAdapter.ViewHolder, position: Int) {
        val data = rankList[position]
        binding?.apply {
            rankAdTvUsername.text = data.username
            rankAdTvRank.text = mContext.getString(R.string.ranking, data.rank)
            rankAdTvCoinCount.text = mContext.getString(R.string.coin, data.coinCount)
            rankAdTvLevel.text = mContext.getString(R.string.lever, data.level)
            rankAdRlItem.setOnClickListener {
                ShareActivity.actionStart(mContext, false, data.userId)
            }
        }
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

}
