package com.zj.play.compose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.core.Play
import com.zj.core.util.showToast
import com.zj.model.model.BaseModel
import com.zj.model.model.Login
import com.zj.play.R
import com.zj.play.compose.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val accountRepository: AccountRepository = AccountRepository()

    private val _state = MutableLiveData<LoginState>()
    val state: LiveData<LoginState>
        get() = _state

    fun toLoginOrRegister(account: Account) {
        _state.postValue(Logging)
        viewModelScope.launch(Dispatchers.IO) {
            val loginModel: BaseModel<Login> = if (account.isLogin) {
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
                _state.postValue(LoginSuccess(login))
                Play.isLogin = true
                Play.setUserInfo(login.nickname, login.username)
                showToast(
                    if (account.isLogin) getApplication<Application>().getString(R.string.login_success) else getApplication<Application>().getString(
                        R.string.register_success
                    )
                )
            } else {
                showToast(loginModel.errorMsg)
                _state.postValue(LoginError)
            }
        }
    }

    private val _logoutState = MutableLiveData<LogoutState>()
    val logoutState: LiveData<LogoutState>
        get() = _logoutState

    fun logout(){
        viewModelScope.launch {
            accountRepository.getLogout()
            Play.logout()
            _logoutState.postValue(LogoutFinish)
        }

    }

}


sealed class LogoutState
object LogoutFinish : LogoutState()
object LogoutDefault : LogoutState()

data class Account(val username: String, val password: String, val isLogin: Boolean)
sealed class LoginState
object Logging : LoginState()
data class LoginSuccess(val login: Login) : LoginState()
object LoginError : LoginState()