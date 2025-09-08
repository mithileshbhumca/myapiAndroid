package com.test.myapiandroid

import android.app.Application
import android.content.Intent
import com.test.myapiandroid.ui.auth.MainActivity

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
    }


    fun logoutUser() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}