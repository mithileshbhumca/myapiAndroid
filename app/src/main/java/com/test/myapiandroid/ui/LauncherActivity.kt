//package com.test.myapiandroid.ui
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import com.test.myapiandroid.ui.auth.AuthViewModel
//
//class LauncherActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val viewModel = AuthViewModel(application)
//
////        setContent {
////            if (viewModel.jwtToken != null) {
////                UsersScreen(viewModel) // ✅ Already logged in
////            } else {
////                LoginScreen(viewModel)
////            }
////        }
//    }
//}