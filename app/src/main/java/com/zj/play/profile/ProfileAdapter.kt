package com.zj.play.profile

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.core.Play
import com.zj.play.R
import com.zj.play.main.LoginActivity
import com.zj.play.article.ArticleActivity
import com.zj.play.article.collect.CollectListActivity
import com.zj.play.profile.history.BrowseHistoryActivity
import com.zj.play.profile.user.UserActivity
import com.zj.play.profile.rank.user.UserRankActivity

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class ProfileAdapter(
    context: Context,
    profileItemList: ArrayList<ProfileItem>,
    layoutId: Int = R.layout.adapter_profile
) :
    CommonAdapter<ProfileItem>(context, layoutId, profileItemList) {

    override fun convert(holder: ViewHolder, t: ProfileItem, position: Int) {
        val profileAdLlItem = holder.getView<LinearLayout>(R.id.profileAdLlItem)
        val profileAdIv = holder.getView<ImageView>(R.id.profileAdIv)
        val profileAdTvTitle = holder.getView<TextView>(R.id.profileAdTvTitle)
        profileAdTvTitle.text = t.title
        profileAdIv.setImageResource(t.imgId)
        profileAdLlItem.setOnClickListener {
            toJump(t.title)
        }
    }

    private fun toJump(title: String) {
        when (title) {
            mContext.getString(R.string.mine_points) -> {
                if (Play.isLogin) {
                    UserRankActivity.actionStart(mContext)
                } else {
                    LoginActivity.actionStart(mContext)
                }
            }
            mContext.getString(R.string.my_collection) -> {
                if (Play.isLogin) {
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
}

data class ProfileItem(var title: String, var imgId: Int)