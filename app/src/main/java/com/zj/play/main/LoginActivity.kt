package com.zj.play.main

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.observe
import com.blankj.utilcode.util.NetworkUtils
import com.zj.core.Play
import com.zj.core.util.LiveDataBus
import com.zj.core.util.showToast
import com.zj.core.view.ActivityCollector
import com.zj.core.view.BaseActivity
import com.zj.play.R
import com.zj.model.model.Login
import com.zj.network.repository.AccountRepository
import com.zj.play.home.LOGIN_REFRESH
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
                showToast(if (isLogin) getString(R.string.login_success) else getString(R.string.register_success))
                LiveDataBus.get().getChannel(LOGIN_REFRESH).setValue(true);
            } else {
                showToast(
                    if (isLogin) getString(R.string.account_password_mismatch) else getString(
                        R.string.user_name_already_registered
                    )
                )
            }
        }
    }

    private fun judge(): Boolean {
        mUserName = loginUserNumberEdit.text.toString()
        mPassWord = loginPassNumberEdit.text.toString()
        if (TextUtils.isEmpty(mUserName) || mUserName.length < 6) {
            loginUserNumberEdit.error = getString(R.string.enter_name_format)
            return false
        }
        if (TextUtils.isEmpty(mPassWord) || mPassWord.length < 6) {
            loginPassNumberEdit.error = getString(R.string.enter_password_format)
            return false
        }
        if (!NetworkUtils.isConnected()) {
            showToast(getString(R.string.no_network))
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
