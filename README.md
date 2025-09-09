# myapiAndroid

# 📱 MyAPI Android – Retrofit Auth Client

This is the **Android client app** that integrates with the backend API.  
It uses **Retrofit + OkHttp Interceptor** to manage **JWT authentication (access + refresh tokens)** securely.

---

## ✨ Features
- 🔑 User login/signup via backend APIs
- ⏳ Secure token storage (SharedPreferences, upgradeable to EncryptedStorage)
- 🔄 Auto refresh access token when expired
- ⛔ Auto logout if refresh token expired
- 📱 Jetpack Compose / MVVM structure (scalable)

---

## 📂 Project Structure
android-app/
├── data/
│ ├── api/ApiService.kt
│ ├── api/AuthInterceptor.kt
│ ├── api/RetrofitClient.kt
│ └── storage/SecureStorage.kt
├── ui/
│ ├── LoginActivity.kt
│ ├── MainActivity.kt
│ └── AuthViewModel.kt
└── App.kt


---

## 🔗 API Integration
- **Login API** → Stores `accessToken`, `refreshToken`, and `refreshTokenExpiry`
- **AuthInterceptor** → 
  - Attaches `Authorization: Bearer <token>` header
  - Refreshes token automatically if expired
  - Logs out if refresh token also expired

---

## ⚙️ Setup
1. Clone repo:
   ```bash
   git clone https://github.com/your-repo/myapi.git
   cd android-app
2. Update BASE_URL in RetrofitClient.kt:
private const val BASE_URL = "http://10.0.2.2:5000/" // For Emulator
3. Run app in Android Studio.

🛠 Tech Stack

Kotlin + MVVM + Jetpack Compose

Retrofit2 + OkHttp

SharedPreferences (SecureStorage)

Coroutine support for API calls

🚀 Future Improvements

Use EncryptedSharedPreferences for tokens

Add biometric authentication (fingerprint/face)

Offline mode with Room database
