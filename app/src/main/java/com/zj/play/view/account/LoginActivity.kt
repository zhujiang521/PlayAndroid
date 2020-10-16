package com.zj.play.view.account

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.observe
import com.blankj.utilcode.util.NetworkUtils
import com.zj.core.Play
import com.zj.core.util.showToast
import com.zj.core.view.ActivityCollector
import com.zj.core.view.BaseActivity
import com.zj.play.R
import com.zj.play.model.Login
import com.zj.play.network.AccountRepository
import com.zj.play.view.article.ArticleBroadCast
import com.zj.play.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity(), View.OnClickListener {

    private var mUserName = ""
    private var mPassWord = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
        loginButton.setOnClickListener(this)
        loginBtnRegister.setOnClickListener(this)
    }

    override fun initView() {

    }

    override fun onClick(v: View) {
        if (!judge()) {
            return
        }
        toProgressVisible(true)
        when (v.id) {
            R.id.loginButton -> {
                toLogin()
            }
            R.id.loginBtnRegister -> {
                toRegister()
            }
        }
    }

    private fun toLogin() {
        AccountRepository.getLogin(mUserName, mPassWord).observe(this) {
            login(true, it)
        }
    }

    private fun toRegister() {
        AccountRepository.getRegister(mUserName, mPassWord, mPassWord).observe(this) {
            login(false, it)
        }
    }

    private fun login(isLogin: Boolean, it: Result<Login>) {
        toProgressVisible(false)
        if (it.isSuccess) {
            val projectTree = it.getOrNull()
            if (projectTree != null) {
                Play.isLogin = true
                Play.setUserInfo(projectTree.nickname, projectTree.username)
                ActivityCollector.finishAll()
                MainActivity.actionStart(this)
                showToast(if (isLogin) "登录成功" else "注册成功")
                ArticleBroadCast.sendArticleChangesReceiver(this)
            } else {
                showToast(if (isLogin) "账号密码不匹配！" else "用户名已被注册！")
            }
        }
    }

    private fun judge(): Boolean {
        mUserName = loginUserNumberEdit.text.toString()
        mPassWord = loginPassNumberEdit.text.toString()
        if (TextUtils.isEmpty(mUserName) || mUserName.length < 6) {
            loginUserNumberEdit.error = "请输入正确的用户名格式"
            return false
        }
        if (TextUtils.isEmpty(mPassWord) || mPassWord.length < 6) {
            loginPassNumberEdit.error = "请输入正确的密码格式"
            return false
        }
        if (!NetworkUtils.isConnected()) {
            showToast("当前网络不可用")
            return false
        }
        return true
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
