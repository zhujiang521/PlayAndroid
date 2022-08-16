package com.zj.play.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zj.core.Play
import com.zj.core.view.base.BaseRecyclerAdapter
import com.zj.play.R
import com.zj.play.article.ArticleActivity
import com.zj.play.article.collect.CollectListActivity
import com.zj.play.databinding.AdapterProfileBinding
import com.zj.play.main.login.LoginActivity
import com.zj.play.profile.history.BrowseHistoryActivity
import com.zj.play.profile.rank.user.UserRankActivity
import com.zj.play.profile.user.UserActivity

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class ProfileAdapter(
    private val mContext: Context,
    private val profileItemList: ArrayList<ProfileItem>,
) : BaseRecyclerAdapter<AdapterProfileBinding>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerHolder<AdapterProfileBinding> {
        val binding =
            AdapterProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseRecyclerHolder(binding)
    }


    override fun onBaseBindViewHolder(position: Int, binding: AdapterProfileBinding) {
        val data = profileItemList[position]
        binding.apply {
            profileAdTvTitle.text = data.title
            profileAdIv.setImageResource(data.imgId)
            profileAdLlItem.setOnClickListener {
                toJump(data.title)
            }
        }
    }

    private fun toJump(title: String) {
        when (title) {
            mContext.getString(R.string.mine_points) -> {
                if (Play.isLoginResult()) {
                    UserRankActivity.actionStart(mContext)
                } else {
                    LoginActivity.actionStart(mContext)
                }
            }
            mContext.getString(R.string.my_collection) -> {
                if (Play.isLoginResult()) {
                    CollectListActivity.actionStart(mContext)
                } else {
                    LoginActivity.actionStart(mContext)
                }
            }
            mContext.getString(R.string.mine_blog) -> {
                ArticleActivity.actionStart(
                    mContext,
                    mContext.getString(R.string.mine_blog),
                    "https://zhujiang.blog.csdn.net/"
                )
            }
            mContext.getString(R.string.browsing_history) -> {
                BrowseHistoryActivity.actionStart(mContext)

            }
            mContext.getString(R.string.mine_nuggets) -> {
                ArticleActivity.actionStart(
                    mContext,
                    mContext.getString(R.string.mine_nuggets),
                    "https://juejin.im/user/5c07e51de51d451de84324d5"
                )
            }
            mContext.getString(R.string.github) -> {
                ArticleActivity.actionStart(
                    mContext,
                    mContext.getString(R.string.mine_github),
                    "https://github.com/zhujiang521"
                )
            }
            mContext.getString(R.string.about_me) -> {
                UserActivity.actionStart(mContext)
            }
        }
    }

    override fun getItemCount(): Int {
        return profileItemList.size
    }

}

data class ProfileItem(var title: String, var imgId: Int)