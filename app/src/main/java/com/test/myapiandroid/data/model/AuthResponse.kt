package com.test.myapiandroid.data.model

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)
