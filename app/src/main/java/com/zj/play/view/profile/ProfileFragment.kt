package com.zj.play.view.profile

import android.app.AlertDialog
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.zj.core.Play
import com.zj.core.Play.logout
import com.zj.core.util.Preference
import com.zj.core.view.BaseFragment
import com.zj.play.R
import com.zj.play.network.AccountRepository
import com.zj.play.view.account.LoginActivity
import com.zj.play.view.article.ArticleBroadCast
import com.zj.play.view.rank.list.RankActivity
import com.zj.play.view.share.ShareActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(), View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    private lateinit var profileAdapter: ProfileAdapter
    private var profileItemList = ArrayList<ProfileItem>()

    override fun initView() {
        profileTitleBar.setRightImage(R.drawable.btn_right_right_bg)
        profileTitleBar.setBackImageVisiable(false)
        profileTitleBar.setRightImgOnClickListener {
            RankActivity.actionStart(context!!)
        }
        profileIvHead.setOnClickListener(this)
        profileTvName.setOnClickListener(this)
        profileTvRank.setOnClickListener(this)
        profileBtnLogout.setOnClickListener(this)
        profileRv.layoutManager = LinearLayoutManager(context)
        profileAdapter = ProfileAdapter(context!!, profileItemList)
        profileRv.adapter = profileAdapter
        if (Play.isLogin) {
            profileIvHead.setBackgroundResource(R.drawable.ic_head)
            profileTvName.text = Play.nickName
            profileTvRank.text = Play.username
            profileBtnLogout.visibility = View.VISIBLE
        } else {
            clearInfo()
        }
    }

    private fun clearInfo() {
        profileBtnLogout.visibility = View.GONE
        profileIvHead.setBackgroundResource(R.drawable.img_nomal_head)
        profileTvName.text = getString(R.string.no_login)
        profileTvRank.text = getString(R.string.click_login)
        ArticleBroadCast.sendArticleChangesReceiver(context!!)
    }

    override fun initData() {
        if (profileItemList.size == 0) {
            profileItemList.add(
                ProfileItem(
                    getString(R.string.mine_points),
                    R.drawable.ic_message_black_24dp
                )
            )
            profileItemList.add(
                ProfileItem(
                    getString(R.string.my_collection),
                    R.drawable.ic_collections_black_24dp
                )
            )
            profileItemList.add(
                ProfileItem(
                    getString(R.string.mine_blog),
                    R.drawable.ic_account_blog_black_24dp
                )
            )
            profileItemList.add(
                ProfileItem(
                    getString(R.string.browsing_history),
                    R.drawable.ic_baseline_history_24
                )
            )
            profileItemList.add(
                ProfileItem(
                    getString(R.string.mine_nuggets),
                    R.drawable.ic_bug_report_black_24dp
                )
            )
            profileItemList.add(ProfileItem("Github", R.drawable.ic_github_black_24dp))
            profileItemList.add(ProfileItem(getString(R.string.about_me), R.drawable.ic_account_circle_black_24dp))
            profileAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.profileIvHead, R.id.profileTvName, R.id.profileTvRank -> personalInformation()
            R.id.profileBtnLogout -> setLogout()
        }
    }

    private fun setLogout() {
        AlertDialog.Builder(context).setTitle(getString(R.string.log_out))
            .setMessage(getString(R.string.sure_log_out))
            .setNegativeButton(
                "取消"
            ) { dialog, _ -> dialog?.dismiss() }
            .setPositiveButton("确定") { dialog, _ ->
                dialog?.dismiss()
                Preference.clear()
                clearInfo()
                logout()
                AccountRepository.getLogout()
            }.show()
    }

    private fun personalInformation() {
        if (!Play.isLogin) {
            LoginActivity.actionStart(context!!)
        } else {
            ShareActivity.actionStart(context!!, true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

}
