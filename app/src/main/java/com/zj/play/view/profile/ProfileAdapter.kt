package com.zj.play.view.profile

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zj.core.Play
import com.zj.core.util.showToast
import com.zj.play.R
import com.zj.play.view.account.LoginActivity
import com.zj.play.view.article.ArticleActivity
import com.zj.play.view.collect.CollectListActivity
import com.zj.play.view.profile.history.BrowseHistoryActivity
import com.zj.play.view.profile.user.UserActivity
import com.zj.play.view.rank.user.UserRankActivity

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class ProfileAdapter(context: Context, layoutId: Int, profileItemList: ArrayList<ProfileItem>) :
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
            "我的积分" -> {
                if (Play.isLogin()) {
                    UserRankActivity.actionStart(mContext)
                } else {
                    LoginActivity.actionStart(mContext)
                }
            }
            "我的收藏" -> {
                if (Play.isLogin()) {
                    CollectListActivity.actionStart(mContext)
                } else {
                    LoginActivity.actionStart(mContext)
                }
            }
            "我的博客" -> {
                ArticleActivity.actionStart(
                    mContext,
                    "我的博客",
                    "https://zhujiang.blog.csdn.net/"
                )
            }
            "浏览历史" -> {
                BrowseHistoryActivity.actionStart(mContext)
            }
            "掘金" -> {
                ArticleActivity.actionStart(
                    mContext,
                    "我的博客",
                    "https://juejin.im/user/5c07e51de51d451de84324d5"
                )
            }
            "Github" -> {
                ArticleActivity.actionStart(
                    mContext,
                    "我的Github",
                    "https://github.com/zhujiang521"
                )
            }
            "关于我" -> {
                UserActivity.actionStart(mContext)
            }
        }
    }
}

data class ProfileItem(var title: String, var imgId: Int)