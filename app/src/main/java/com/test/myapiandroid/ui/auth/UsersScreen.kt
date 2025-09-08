package com.test.myapiandroid.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.test.myapiandroid.data.model.User
import com.test.myapiandroid.viewmodel.AuthViewModel

@Composable
fun UsersScreen(onLogout: () -> Unit, viewModel: AuthViewModel) {
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getUsers { ok, data ->
            loading = false
            if (ok) users = data ?: emptyList() else error = "Failed to fetch users"
        }
    }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Users", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = { viewModel.logout(); onLogout() }) { Text("Logout") }
        }

        if (loading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text(error!!, color = MaterialTheme.colorScheme.error)
        } else {
            users.forEach { u -> Text("• ${u.name} (${u.email})") }
        }
    }
}
