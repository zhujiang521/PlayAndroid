package com.zj.play.view.account

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import com.zj.core.Play
import com.zj.core.util.showToast
import com.zj.core.view.ActivityCollector
import com.zj.core.view.BaseActivity
import com.zj.play.R
import com.zj.play.network.Repository
import com.zj.play.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
        loginButton.setOnClickListener(this)
    }

    override fun initView() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> {
                toLogin()
            }
        }
    }

    private fun toLogin() {
        val userName = loginUserNumberEdit.text.toString()
        val passWord = loginPassNumberEdit.text.toString()
        if (TextUtils.isEmpty(userName) || userName.length < 6) {
            loginUserNumberEdit.error = "请输入正确的用户名格式"
            return
        }
        if (TextUtils.isEmpty(passWord) || passWord.length < 6) {
            loginPassNumberEdit.error = "请输入正确的密码格式"
            return
        }
        toProgressVisible(true)
        Repository.getLogin(userName, passWord).observe(this, Observer {
            toProgressVisible(false)
            if (it.isSuccess) {
                val projectTree = it.getOrNull()
                if (projectTree != null) {
                    Play.setLogin(true)
                    Play.setUserInfo(projectTree.nickname, projectTree.username)
                    ActivityCollector.finishAll()
                    MainActivity.actionStart(this)
                    showToast("登录成功")
                } else {
                    showToast("账号密码不匹配！")
                }
            } else {
                showToast("账号密码不匹配！")
            }
        })
    }

    private fun toProgressVisible(b: Boolean) {
        loginProgressBar.visibility = if (b) View.VISIBLE else View.INVISIBLE
        loginInputElements.visibility = if (!b) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

}
