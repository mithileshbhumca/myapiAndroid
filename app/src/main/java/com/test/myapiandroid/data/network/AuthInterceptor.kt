package com.test.myapiandroid.data.network

import com.test.myapiandroid.App
import com.test.myapiandroid.data.storage.SecureStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val storage: SecureStorage,
    private val tokenRefreshApiService: ApiService, // Changed: direct ApiService for refreshing
) : Interceptor {

    @Volatile private var accessToken: String? = storage.getAccessToken()
    private var refreshToken: String? = storage.getRefreshToken()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // Add header if we have an access token
        accessToken?.let {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $it")
                .build()
        }

        val response = chain.proceed(request)

        if (response.code == 403 && refreshToken != null) { // Or 401 depending on your API
            synchronized(this) {
                // Check if token was refreshed by another thread while waiting for synchronized block
                val currentTokenInStorage = storage.getAccessToken()
                if (currentTokenInStorage != null && currentTokenInStorage != accessToken) {
                    accessToken = currentTokenInStorage // Update current access token
                    // Retry with the new token obtained by another thread
                    val newRequest = request.newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer $accessToken")
                        .build()
                    response.close() // Close original response
                    return chain.proceed(newRequest)
                }

                // Proceed with token refresh
                // Check refresh token expiry locally
                if (System.currentTimeMillis() > storage.getRefreshExpiry()) {
                    storage.clearTokens()
                    App.instance.logoutUser() // Make sure this is thread-safe or runs on UI thread
                    return response // Original response (e.g., 403 Forbidden)
                }

                val refreshResponseCall = runBlocking { // Use the dedicated ApiService
                    tokenRefreshApiService.refresh(mapOf("refreshToken" to refreshToken!!))
                }

                if (refreshResponseCall.isSuccessful) {
                    val newAccessToken = refreshResponseCall.body()?.accessToken
                    val newRefreshToken = refreshResponseCall.body()?.refreshToken // Some APIs might return a new refresh token
                    val expiry = refreshResponseCall.body()?.refreshTokenExpiry ?: (System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000) // Ensure expiry is calculated correctly

                    if (newAccessToken != null) {
                        accessToken = newAccessToken
                        // Save the new refresh token if it's provided, otherwise reuse the old one
                        storage.saveTokens(newAccessToken, newRefreshToken ?: refreshToken!!, expiry)

                        // Retry with new token
                        val newRequest = request.newBuilder()
                            .removeHeader("Authorization")
                            .addHeader("Authorization", "Bearer $newAccessToken")
                            .build()
                        response.close() // Close original response before retrying
                        return chain.proceed(newRequest)
                    } else {
                        // Refresh was successful but no new access token, treat as failure
                        storage.clearTokens()
                        App.instance.logoutUser()
                        return response // Original response
                    }
                } else {
                    // Refresh failed (e.g., refresh token expired on server, invalid grant)
                    storage.clearTokens()
                    App.instance.logoutUser()
                    return response // Original response
                }
            }
        }
        return response
    }
}
