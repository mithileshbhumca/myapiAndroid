package com.test.myapiandroid.data.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val refreshTokenExpiry: Long? = null // seconds (optional)
)


