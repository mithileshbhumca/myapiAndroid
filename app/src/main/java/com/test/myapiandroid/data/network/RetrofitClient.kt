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
     private const val BASE_URL = "https://myapi-dwaw.onrender.com/" // TODO: replace with your API URL

    private val storage: SecureStorage by lazy { SecureStorage(App.instance.applicationContext) }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // This is the ApiService that AuthInterceptor will use.
    // It's initialized lazily and uses a *different* OkHttpClient
    // that does NOT include the AuthInterceptor, to avoid recursion for token refresh.
    private val refreshApiService: ApiService by lazy {
        val refreshOkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging) // Add logging, but not AuthInterceptor
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(refreshOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private val mainRetrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            // Pass the refreshApiService to AuthInterceptor
            .addInterceptor(AuthInterceptor(storage, refreshApiService))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy { mainRetrofit.create(ApiService::class.java) }
}
