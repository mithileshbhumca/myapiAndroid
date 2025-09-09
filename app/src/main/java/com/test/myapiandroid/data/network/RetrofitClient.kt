package com.test.myapiandroid.data.network

import com.test.myapiandroid.App
import com.test.myapiandroid.data.storage.SecureStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    //    private const val BASE_URL = "http://localhost:3000/" // TODO: replace with your API URL
    private const val BASE_URL =
        "https://myapi-dwaw.onrender.com/" // TODO: replace with your API URL

    private val storage: SecureStorage by lazy { SecureStorage(App.instance.applicationContext) }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val refreshApiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkhttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private val mainRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkhttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
//            .addInterceptor(AuthInterceptor(storage, apiService) {
//                App.instance.logoutUser()
//            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun reload() {
        // mainRetrofit = null
        //retrofitPreLoginMobile = null
    }

    val apiService: ApiService by lazy { mainRetrofit.create(ApiService::class.java) }
}
