package com.zj.play.profile.user

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import com.zj.core.view.base.BaseActivity
import com.zj.play.R
import com.zj.play.main.startNewActivity

class UserActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_user_info

    override fun initView() {}

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, UserActivity::class.java)
            context.startNewActivity(intent)
        }
    }

}
