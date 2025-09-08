
package com.test.myapiandroid.ui.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.test.myapiandroid.viewmodel.AuthViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm = AuthViewModel(application)

            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var message by remember { mutableStateOf("") }

            Column {
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
                Button(onClick = {
                    vm.login(email, password) { success, msg ->
                        message = msg
                    }
                }) {
                    Text("Login")
                }
                Text(message)
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MyApiAndroidTheme {
//        //Greeting("Android")
//    }
//}
