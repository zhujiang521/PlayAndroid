package com.zj.play.view.profile

import android.app.AlertDialog
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.zj.core.Play
import com.zj.core.Play.logout
import com.zj.core.islogin.NeedLogin
import com.zj.core.islogin.NeedLogin.SHOW_DIALOG
import com.zj.core.view.BaseFragment
import com.zj.play.R
import com.zj.play.network.Repository
import com.zj.play.view.account.LoginActivity
import com.zj.play.view.rank.list.RankActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(), View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    private lateinit var profileAdapter: ProfileAdapter
    private var profileItemList = ArrayList<ProfileItem>()

    override fun initView() {
        profileTitleBar.setRightImage(R.drawable.ic_equalizer_black_24dp)
        profileTitleBar.setBackImageVisiable(false)
        profileTitleBar.setRightImgOnClickListener(View.OnClickListener {
            RankActivity.actionStart(context!!)
        })
        profileIvHead.setOnClickListener(this)
        profileTvName.setOnClickListener(this)
        profileTvRank.setOnClickListener(this)
        profileBtnLogout.setOnClickListener(this)
        profileRv.layoutManager = LinearLayoutManager(context)
        profileAdapter = ProfileAdapter(context!!, R.layout.adapter_profile, profileItemList)
        profileRv.adapter = profileAdapter
        if (Play.isLogin()) {
            profileIvHead.setImageResource(R.drawable.ic_head)
            profileTvName.text = Play.getNickName()
            profileTvRank.text = Play.getUsername()
            profileBtnLogout.visibility = View.VISIBLE
        } else {
            clearInfo()
        }
    }

    private fun clearInfo() {
        profileBtnLogout.visibility = View.GONE
        profileIvHead.setImageResource(R.drawable.img_nomal_head)
        profileTvName.text = "未登录"
        profileTvRank.text = "请点击进行登录"
    }

    override fun initData() {
        if (profileItemList.size == 0) {
            profileItemList.add(ProfileItem("我的积分", R.drawable.ic_message_black_24dp))
            profileItemList.add(ProfileItem("我的收藏", R.drawable.ic_collections_black_24dp))
            profileItemList.add(ProfileItem("我的博客", R.drawable.ic_account_blog_black_24dp))
            profileItemList.add(ProfileItem("浏览历史", R.drawable.ic_baseline_history_24))
            profileItemList.add(ProfileItem("掘金", R.drawable.ic_bug_report_black_24dp))
            profileItemList.add(ProfileItem("Github", R.drawable.ic_github_black_24dp))
            profileItemList.add(ProfileItem("关于我", R.drawable.ic_account_circle_black_24dp))
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
        AlertDialog.Builder(context).setTitle("退出登录").setMessage("确定要退出登录吗？")
            .setNegativeButton(
                "取消"
            ) { dialog, _ -> dialog?.dismiss() }
            .setPositiveButton("确定") { dialog, _ ->
                dialog?.dismiss()
                clearInfo()
                logout()
                Repository.getLogout()
            }.show()
    }

    @NeedLogin(tipType = SHOW_DIALOG, LoginActivity = LoginActivity::class)
    private fun personalInformation() {
        if (!Play.isLogin()) {
            LoginActivity.actionStart(context!!)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

}
