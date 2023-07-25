package com.zj.play.ui.view.edit

import java.util.regex.Pattern

private const val PASSWORD_VALIDATION_REGEX = "^[A-Za-z0-9]{6,}"

class PasswordState :
    TextFieldState(validator = ::isPasswordValid, errorFor = ::passwordValidationError)

private fun isPasswordValid(password: String): Boolean {
    return Pattern.matches(PASSWORD_VALIDATION_REGEX, password)
}

private fun passwordValidationError(password: String): String {
    if (password.isEmpty()) {
        return "error: please enter password"
    }
    return "error:wrong password format"
}