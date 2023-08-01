package com.zj.play.ui.page.login

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zj.model.BaseModel
import com.zj.model.LoginModel
import com.zj.model.PlayError
import com.zj.model.PlayLoading
import com.zj.model.PlayState
import com.zj.model.PlaySuccess
import com.zj.play.Play
import com.zj.play.R
import com.zj.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/17
 * 描述：PlayAndroid
 *
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val accountRepository: AccountRepository = AccountRepository()

    private val _state = mutableStateOf<PlayState<LoginModel>>(PlayLoading)
    val state: State<PlayState<LoginModel>>
        get() = _state

    fun toLoginOrRegister(account: Account) {
        _state.value = PlayLoading
        viewModelScope.launch(Dispatchers.IO) {
            val loginModel: BaseModel<LoginModel> = if (account.isLogin) {
                accountRepository.getLogin(account.username, account.password)
            } else {
                accountRepository.getRegister(
                    account.username,
                    account.password,
                    account.password
                )
            }

            if (loginModel.errorCode == 0) {
                val login = loginModel.data
                _state.value = PlaySuccess(login)
                Play.isLogin = true
                Play.setUserInfo(login.nickname, login.username)
                withContext(Dispatchers.Main) {
                    showToast(
                        getApplication(),
                        if (account.isLogin) getApplication<Application>().getString(R.string.login_success) else getApplication<Application>().getString(
                            R.string.register_success
                        )
                    )
                }
            } else {
                showToast(getApplication(), loginModel.errorMsg)
                _state.value = PlayError(NullPointerException("网络错误"))
            }
        }
    }

    private val _logoutState = mutableStateOf<LogoutState>(LogoutDefault)
    val logoutState: State<LogoutState>
        get() = _logoutState

    fun logout() {
        viewModelScope.launch {
            accountRepository.getLogout()
            Play.logout()
            _logoutState.value = LogoutFinish
        }
    }

}


sealed class LogoutState
data object LogoutFinish : LogoutState()
data object LogoutDefault : LogoutState()

data class Account(val username: String, val password: String, val isLogin: Boolean)