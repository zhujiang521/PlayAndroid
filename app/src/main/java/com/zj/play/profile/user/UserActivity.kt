package com.zj.play.profile.user

import android.content.Context
import android.content.Intent
import com.zj.core.view.base.BaseActivity
import com.zj.play.R

class UserActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_user_info

    override fun initView() {}

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, UserActivity::class.java)
            context.startActivity(intent)
        }
    }

}
