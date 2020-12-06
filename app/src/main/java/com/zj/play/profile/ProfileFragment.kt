package com.zj.play.profile

import android.app.AlertDialog
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.zj.core.Play
import com.zj.core.Play.logout
import com.zj.core.util.Preference
import com.zj.core.view.base.BaseFragment
import com.zj.play.R
import com.zj.network.repository.AccountRepository
import com.zj.play.main.LoginActivity
import com.zj.play.article.ArticleBroadCast
import com.zj.play.profile.rank.list.RankActivity
import com.zj.play.profile.share.ShareActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(), View.OnClickListener {

    override fun getLayoutId(): Int = R.layout.fragment_profile

    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var nameArray: Array<String>
    private var profileItemList = ArrayList<ProfileItem>()
    private val imageArray = arrayOf(
        R.drawable.ic_message_black_24dp,
        R.drawable.ic_collections_black_24dp,
        R.drawable.ic_account_blog_black_24dp,
        R.drawable.ic_baseline_history_24,
        R.drawable.ic_bug_report_black_24dp,
        R.drawable.ic_github_black_24dp,
        R.drawable.ic_account_circle_black_24dp
    )

    override fun initView() {
        profileTitleBar.setRightImage(R.drawable.btn_right_right_bg)
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
            nameArray = arrayOf(
                getString(R.string.mine_points),
                getString(R.string.my_collection),
                getString(R.string.mine_blog),
                getString(R.string.browsing_history),
                getString(R.string.mine_nuggets),
                getString(R.string.github),
                getString(R.string.about_me)
            )
            for (index in nameArray.indices) {
                profileItemList.add(
                    ProfileItem(nameArray[index], imageArray[index])
                )
            }
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
                getString(R.string.cancel)
            ) { dialog, _ -> dialog?.dismiss() }
            .setPositiveButton(getString(R.string.sure)) { dialog, _ ->
                dialog?.dismiss()
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
