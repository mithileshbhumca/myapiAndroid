package com.test.myapiandroid.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.test.myapiandroid.viewmodel.AuthViewModel

@Composable
fun LoginScreen(onLoggedIn: () -> Unit, viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Welcome", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                viewModel.login(email, password) { ok, msg ->
                    message = msg
                    if (ok) onLoggedIn()
                }
            }) { Text("Login") }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name (for signup)") }, modifier = Modifier.width(200.dp))
            Button(onClick = {
                viewModel.signup(name, email, password) { ok, msg -> message = msg }
            }) { Text("Signup") }
        }

        if (message.isNotEmpty()) Text(message, color = MaterialTheme.colorScheme.primary)
    }
}
