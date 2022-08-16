package com.zj.play.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zj.core.Play
import com.zj.core.Play.logout
import com.zj.play.R
import com.zj.play.article.ArticleBroadCast
import com.zj.play.databinding.FragmentProfileBinding
import com.zj.play.home.ArticleCollectBaseFragment
import com.zj.play.main.login.AccountRepository
import com.zj.play.main.login.LoginActivity
import com.zj.play.profile.rank.list.RankActivity
import com.zj.play.profile.share.ShareActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : ArticleCollectBaseFragment(), View.OnClickListener {

    private var binding: FragmentProfileBinding? = null

    @Inject
    lateinit var accountRepository: AccountRepository

    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var nameArray: Array<String>
    private var profileItemList = ArrayList<ProfileItem>()
    private var dialog: AlertDialog? = null
    private val imageArray = arrayOf(
        R.drawable.ic_integral,
        R.drawable.ic_profile_collect,
        R.drawable.ic_csdn,
        R.drawable.ic_history,
        R.drawable.ic_cnblogs,
        R.drawable.ic_github,
        R.drawable.ic_profile_mine
    )

    override fun getLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun initView() {
        binding?.apply {
            profileTitleBar.setRightImage(R.drawable.btn_right_right_bg)
            profileTitleBar.setRightImgOnClickListener {
                RankActivity.actionStart(requireContext())
            }
            profileIvHead.setOnClickListener(this@ProfileFragment)
            profileTvName.setOnClickListener(this@ProfileFragment)
            profileTvRank.setOnClickListener(this@ProfileFragment)
            profileBtnLogout.setOnClickListener(this@ProfileFragment)
            profileRv.layoutManager = LinearLayoutManager(context)
            profileAdapter = ProfileAdapter(requireContext(), profileItemList)
            profileRv.adapter = profileAdapter
            refreshData()
        }
    }

    override fun refreshData() {
        lifecycleScope.launch {
            Play.isLogin().collectLatest {
                binding?.apply {
                    if (it) {
                        profileIvHead.setImageResource(R.drawable.ic_head)
                        profileTvName.text = Play.nickName
                        profileTvRank.text = Play.username
                        profileBtnLogout.visibility = View.VISIBLE
                    } else {
                        clearInfo()
                    }
                }
            }
        }
    }

    private fun clearInfo() {
        binding?.apply {
            profileBtnLogout.visibility = View.GONE
            profileIvHead.setImageResource(R.drawable.img_nomal_head)
            profileTvName.text = getString(R.string.no_login)
            profileTvRank.text = getString(R.string.click_login)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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
            R.id.logout_cancel -> dialog?.dismiss()
            R.id.logout_sure -> {
                dialog?.dismiss()
                clearInfo()
                logout()
                ArticleBroadCast.sendArticleChangesReceiver(requireContext())
                accountRepository.getLogout()
            }
        }
    }

    private fun setLogout() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_logout, null)
        view.apply {
            findViewById<TextView>(R.id.logout_cancel).setOnClickListener(this@ProfileFragment)
            findViewById<TextView>(R.id.logout_sure).setOnClickListener(this@ProfileFragment)
        }
        dialog = AlertDialog.Builder(context).setView(view).create()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.show()
    }

    private fun personalInformation() {
        if (!Play.isLoginResult()) {
            LoginActivity.actionStart(requireContext())
        } else {
            ShareActivity.actionStart(requireContext(), true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

}
