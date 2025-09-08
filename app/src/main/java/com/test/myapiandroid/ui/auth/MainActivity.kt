package com.test.myapiandroid.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.test.myapiandroid.App
import com.test.myapiandroid.data.storage.SecureStorage
import com.test.myapiandroid.ui.theme.MyApiAndroidTheme
import com.test.myapiandroid.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApiAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val storage = SecureStorage(applicationContext)
                    val viewModel = AuthViewModel(application)
                    MyUI(storage,viewModel)

                }
            }
        }
    }
}

@Composable
fun MyUI(storage: SecureStorage, viewModel: AuthViewModel) {
    var isLoggedIn by remember { mutableStateOf(storage.getAccessToken() != null) }
    Box(Modifier.fillMaxSize()) {
        if (isLoggedIn) {
            UsersScreen(
                onLogout = {
                    viewModel.logout()
                    isLoggedIn = false
                },
                viewModel = viewModel
            )
        } else {
            LoginScreen(
                onLoggedIn = {
                    isLoggedIn = true
                },
                viewModel = viewModel
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MyPreview() {
    MyApiAndroidTheme {
        val storage = SecureStorage(App.instance)
        val viewModel = AuthViewModel(App.instance)
        MyUI(storage,viewModel)
    }
}

