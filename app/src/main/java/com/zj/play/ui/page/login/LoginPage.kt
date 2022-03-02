package com.zj.play.ui.page.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zj.model.*
import com.zj.play.R
import com.zj.play.ui.main.nav.PlayActions
import com.zj.play.ui.view.bar.PlayAppBar
import com.zj.play.ui.view.edit.*
import kotlinx.coroutines.launch

sealed class SignInEvent {
    data class SignIn(val email: String, val password: String) : SignInEvent()
    object SignUp : SignInEvent()
    object SignInAsGuest : SignInEvent()
    object NavigateBack : SignInEvent()
}

@Composable
fun LoginPage(actions: PlayActions) {
    val viewModel: LoginViewModel = hiltViewModel()
    val loginState by viewModel.state.observeAsState()
    LoginPageContent(actions, loginState, {
        viewModel.logout()
    }) {
        viewModel.toLoginOrRegister(it)
    }
}

@Composable
fun LoginPageContent(
    onNavigationEvent: PlayActions,
    loginState: PlayState<LoginModel>?,
    toLogout: () -> Unit,
    toLoginOrRegister: (Account) -> Unit,
) {

    SignIn(onNavigationEvent = { event ->
        when (event) {
            is SignInEvent.SignIn -> {
                toLoginOrRegister(Account(event.email, event.password, true))
            }
            SignInEvent.SignUp -> {
                toLogout()
            }
            SignInEvent.SignInAsGuest -> {
                toLogout()
            }
            SignInEvent.NavigateBack -> {
                onNavigationEvent.upPress()
            }
        }
    })
    when (loginState) {
        is PlayLoading -> {
            //toProgressVisible(true)
        }
        is PlaySuccess -> {
            //toProgressVisible(false)
            onNavigationEvent.upPress()
        }
        is PlayError -> {
            //toProgressVisible(false)
        }
        else -> {}
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignIn(onNavigationEvent: (SignInEvent) -> Unit) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val snackBarErrorText = stringResource(id = R.string.feature_not_available)
    val snackBarActionLabel = stringResource(id = R.string.dismiss)

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        topBar = {
            PlayAppBar(
                title = stringResource(id = R.string.sign_in),
                click = { onNavigationEvent(SignInEvent.NavigateBack) })
        },
        content = {
            SignInSignUpScreen(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SignInContent(
                        onSignInSubmitted = { email, password ->
                            onNavigationEvent(SignInEvent.SignIn(email, password))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = {
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message = snackBarErrorText,
                                    actionLabel = snackBarActionLabel
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.forgot_password))
                    }
                }
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ErrorSnackBar(
            snackBarHostState = snackBarHostState,
            onDismiss = { snackBarHostState.currentSnackbarData?.dismiss() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun SignInContent(
    onSignInSubmitted: (email: String, password: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        val emailState = remember { EmailState() }
        Email(emailState, onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        val passwordState = remember { PasswordState() }
        Password(
            label = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = { onSignInSubmitted(emailState.text, passwordState.text) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSignInSubmitted(emailState.text, passwordState.text) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                disabledBackgroundColor = MaterialTheme.colors.primary.copy(
                    alpha = 0.82f,
                ),
            ),
            modifier = Modifier
                .fillMaxWidth(),
            enabled = emailState.isValid && passwordState.isValid,
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(
                width = 1.dp, color = MaterialTheme.colors.primary.copy(
                    alpha = 0.82f, red = 0.7f
                )
            )
        ) {
            Text(
                text = stringResource(id = R.string.sign_in)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorSnackBar(
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackBarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2
                    )
                },
                action = {
                    data.actionLabel?.let {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(id = R.string.dismiss),
                                color = MaterialTheme.colors.error
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}
