package com.test.myapiandroid.data.network

import com.test.myapiandroid.App
import com.test.myapiandroid.data.network.RetrofitClient.apiService
import com.test.myapiandroid.data.storage.SecureStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(
    private val storage: SecureStorage,
    private val tokenRefreshApiService: ApiService, // Changed: direct ApiService for refreshing
    private val onLogout: () -> Unit

) : Interceptor {

    @Volatile
    private var accessToken: String? = storage.getAccessToken()
    private var refreshToken: String? = storage.getRefreshToken()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        // 1️⃣ Check refresh token expiry before request
        if (System.currentTimeMillis() > storage.getRefreshExpiry()) {
            storage.clearTokens()
            onLogout()
            throw IOException("Refresh token expired. Please login again.")
        }


        // 2️⃣ Add access token,Add header if we have an access token
        accessToken?.let {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $it")
                .build()
        }

        val response = chain.proceed(request)

        // 3️⃣ If access token expired, try refresh

        if (response.code == 403 && refreshToken != null) { // Or 401 depending on your API
            synchronized(this) {
                // Check if token was refreshed by another thread while waiting for synchronized block
                val currentTokenInStorage = storage.getAccessToken()
                if (currentTokenInStorage != null && currentTokenInStorage != accessToken) {
                    accessToken = currentTokenInStorage // Update current access token
                } else {

                    val refreshResponse = runBlocking { // Use the dedicated ApiService
                        tokenRefreshApiService.refresh(mapOf("refreshToken" to refreshToken!!))
                    }
                    if (refreshResponse.isSuccessful) {
                        accessToken = refreshResponse.body()?.accessToken
                        val expiry = storage.getRefreshExpiry() // already stored
                        storage.saveTokens(
                            accessToken!!,
                            refreshToken!!,
                            (expiry - System.currentTimeMillis()) / 1000
                        )
                    } else {
                        // 🔴 Refresh token rejected by server
                        storage.clearTokens()
                        onLogout()
                        return response
                    }
                }
            }
            // Retry original request with new access token
            accessToken?.let {
                val newRequest = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $it")
                    .build()
                return chain.proceed(newRequest)
            }
        }
        return response
    }
}
