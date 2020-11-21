package com.zj.play.main

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.observe
import com.blankj.utilcode.util.NetworkUtils
import com.zj.core.Play
import com.zj.core.util.LiveDataBus
import com.zj.core.util.showToast
import com.zj.core.view.base.ActivityCollector
import com.zj.core.view.base.BaseActivity
import com.zj.play.R
import com.zj.model.model.Login
import com.zj.network.repository.AccountRepository
import com.zj.play.home.LOGIN_REFRESH
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity(), View.OnClickListener {

    private var mUserName = ""
    private var mPassWord = ""
    private var mIsLogin = true

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initView() {
        loginButton.setOnClickListener(this)
        loginBtnRegister.setOnClickListener(this)
        loginTvRegister.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginTvRegister -> {
                updateState()
            }
            R.id.loginButton -> {
                loginOrRegister(true)
            }
            R.id.loginBtnRegister -> {
                loginOrRegister(false)
            }
        }
    }

    private fun loginOrRegister(isToLogin: Boolean) {
        if (!judge()) return
        toProgressVisible(true)
        if (isToLogin) toLogin() else toRegister()
    }

    private fun updateState() {
        loginTvRegister.text =
            if (mIsLogin) getString(R.string.return_login) else getString(R.string.register_account)
        loginButton.visibility = if (mIsLogin) View.GONE else View.VISIBLE
        loginBtnRegister.visibility = if (mIsLogin) View.VISIBLE else View.GONE
        mIsLogin = !mIsLogin
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
