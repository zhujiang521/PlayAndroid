package com.zj.play.profile.user

import android.content.Context
import android.content.Intent
import android.view.View
import com.zj.core.view.base.BaseActivity
import com.zj.play.R
import com.zj.play.databinding.ActivityUserInfoBinding

class UserActivity : BaseActivity() {

    override fun getLayoutView(): View {
        return ActivityUserInfoBinding.inflate(layoutInflater).root
    }

    override fun initView() {}

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, UserActivity::class.java)
            context.startActivity(intent)
        }
    }

}
