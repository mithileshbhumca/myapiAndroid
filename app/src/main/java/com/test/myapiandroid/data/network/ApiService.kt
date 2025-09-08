package com.test.myapiandroid.data.network

import com.test.myapiandroid.data.model.LoginResponse
import com.test.myapiandroid.data.model.RefreshResponse
import com.test.myapiandroid.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>


    @POST("signup")
    suspend fun signup(@Body body: Map<String, String>): Response<Map<String, Any>>


    @POST("refresh")
    suspend fun refresh(@Body body: Map<String, String>): Response<RefreshResponse>


    @GET("users")
    suspend fun getUsers(@Header("Authorization") token: String): Response<List<User>>


    @GET("hello")
    suspend fun hello(): Response<Map<String, String>>
}

