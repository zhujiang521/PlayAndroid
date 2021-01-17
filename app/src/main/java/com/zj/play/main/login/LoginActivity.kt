package com.zj.play.main.login

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.blankj.utilcode.util.NetworkUtils
import com.zj.core.util.showToast
import com.zj.core.view.base.ActivityCollector
import com.zj.core.view.base.BaseActivity
import com.zj.play.R
import com.zj.play.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {

    private val viewModel by viewModels<LoginViewModel>()
    private var mUserName = ""
    private var mPassWord = ""
    private var mIsLogin = true

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initView() {
        loginButton.setOnClickListener(this)
        loginTvRegister.setOnClickListener(this)
        @Suppress("COMPATIBILITY_WARNING")
        viewModel.state.observe(this) {
            when (it) {
                Logging -> {
                    toProgressVisible(true)
                }
                is LoginSuccess -> {
                    toProgressVisible(false)
                    ActivityCollector.finishAll()
                    MainActivity.actionStart(this)
                }
                LoginError -> {
                    toProgressVisible(false)
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginTvRegister -> {
                flipAnimatorXViewShow(loginInputElements)
            }
            R.id.loginButton -> {
                loginOrRegister()
            }
        }
    }

    private fun loginOrRegister() {
        if (!judge()) return
        viewModel.toLoginOrRegister(Account(mUserName, mPassWord, mIsLogin))
    }

    private fun updateState() {
        loginTvRegister.text =
            if (mIsLogin) getString(R.string.return_login) else getString(R.string.register_account)
        loginButton.text =
            if (mIsLogin) getString(R.string.register_account) else getString(R.string.login)
        mIsLogin = !mIsLogin
    }

    private fun flipAnimatorXViewShow(view: View) {
        val animator1 = ObjectAnimator.ofFloat(view, "rotationY", 0f, if (mIsLogin) 90f else -90f)
        val animator2 = ObjectAnimator.ofFloat(view, "rotationY", if (mIsLogin) -90f else 90f, 0f)
        animator2.interpolator = OvershootInterpolator(2.0f)
        animator1.setDuration(700).start()
        animator1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                animator2.setDuration(700).start()
                updateState()
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
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

    private fun toProgressVisible(visible: Boolean) {
        loginProgressBar.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        loginInputElements.visibility = if (!visible) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

}
