package com.test.myapiandroid.utils

import android.util.Patterns

object ValidationHelper {

    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email cannot be empty"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email address"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 4 -> "Password must be at least 4 characters"
            else -> null
        }
    }

}