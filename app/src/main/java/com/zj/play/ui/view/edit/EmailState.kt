package com.zj.play.ui.view.edit

import java.util.regex.Pattern

private const val EMAIL_VALIDATION_REGEX = "^[A-Za-z0-9]{6,}"

class EmailState :
    TextFieldState(validator = ::isEmailValid, errorFor = ::emailValidationError)

/**
 * Returns an error to be displayed or null if no error was found
 */
private fun emailValidationError(email: String): String {
    return "error: $email"
}

private fun isEmailValid(email: String): Boolean {
    return Pattern.matches(EMAIL_VALIDATION_REGEX, email)
}
