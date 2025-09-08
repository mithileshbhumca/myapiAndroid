package com.test.myapiandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.test.myapiandroid.data.model.User
import com.test.myapiandroid.data.network.RetrofitClient
import com.test.myapiandroid.data.storage.SecureStorage
import kotlinx.coroutines.launch

open class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = SecureStorage(application.applicationContext)
    private val api = RetrofitClient.apiService

    open fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = api.login(mapOf("email" to email, "password" to password))
                if (resp.isSuccessful && resp.body()?.success == true) {
                    val body = resp.body()!!
                    val access = body.accessToken
                    val refresh = body.refreshToken
                    if (access != null && refresh != null) {
                        val expiry = body.refreshTokenExpiry ?: (7 * 24 * 60 * 60)
                        storage.saveTokens(access, refresh, expiry)
                        onResult(true, "Login success")
                    } else onResult(false, "Token missing")
                } else onResult(false, resp.body()?.message ?: "Login failed")
            } catch (e: Exception) { onResult(false, e.message ?: "Error") }
        }
    }

    open fun signup(name: String, email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = api.signup(mapOf("name" to name, "email" to email, "password" to password))
                if (resp.isSuccessful) onResult(true, "Signup success") else onResult(false, "Signup failed")
            } catch (e: Exception) { onResult(false, e.message ?: "Error") }
        }
    }

    fun getUsers(onResult: (Boolean, List<User>?) -> Unit) {
        viewModelScope.launch {
            try {
                val token = storage.getAccessToken()
                if (token == null) { onResult(false, null); return@launch }
                val resp = api.getUsers("Bearer $token")
                if (resp.isSuccessful) onResult(true, resp.body()) else onResult(false, null)
            } catch (_: Exception) { onResult(false, null) }
        }
    }

    fun logout() { storage.clearTokens() }
}
