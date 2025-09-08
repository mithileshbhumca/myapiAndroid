package com.test.myapiandroid.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.myapiandroid.utils.ValidationHelper.validateEmail
import com.test.myapiandroid.utils.ValidationHelper.validatePassword
import com.test.myapiandroid.viewmodel.AuthViewModel

@Composable
fun LoginScreen(onLoggedIn: () -> Unit, viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val isFormValid = emailError == null && passwordError == null &&
            email.isNotBlank() && password.isNotBlank()

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Welcome", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = validateEmail(it)
            },
            label = { Text("Email") },
            isError = emailError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (emailError != null) {
            Text(
                text = emailError!!,
                color = Color.Red,
                fontSize = 12.sp
            )
        }
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = validatePassword(it)
            },
            label = { Text("Password") },
            isError = passwordError != null,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                color = Color.Red,
                fontSize = 12.sp
            )
        }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name (for signup)") },
            modifier = Modifier.width(200.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Login button
            Button(
                onClick = {
                    viewModel.login(email, password) { success, msg ->
                        message = msg
                        if (success) onLoggedIn()
                    }
                },
                enabled = isFormValid && name.isEmpty(),
            ) { Text("Login") }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    viewModel.signup(name, email, password) { ok, msg -> message = msg }
                },
                enabled = name.isNotEmpty() && isFormValid
            ) { Text("Signup") }
        }

        if (message.isNotEmpty()) Text(message, color = Color.Red)
    }
}

