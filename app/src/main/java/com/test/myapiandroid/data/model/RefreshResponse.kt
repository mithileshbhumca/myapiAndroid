package com.test.myapiandroid.data.model

data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
    val refreshTokenExpiry: Long // add this
)