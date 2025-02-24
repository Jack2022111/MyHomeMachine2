package com.example.myhomemachine.network

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class AuthViewModel : ViewModel() {
    private val client = OkHttpClient()
    private val BASE_URL = "http://10.0.2.2:5000/api/auth" // Use this for Android emulator
    // private val BASE_URL = "http://127.0.0.1:5000/api/auth" // Use this for direct device testing

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val message: String) : AuthState()
        data class Error(val message: String) : AuthState()
        data class PasswordResetRequested(val message: String) : AuthState()
        data class PasswordResetSuccess(val message: String) : AuthState()
    }

    // Sign up function
    fun signup(email: String, password: String, onResult: (AuthState) -> Unit) {
        onResult(AuthState.Loading)

        val json = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/signup")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && jsonResponse != null) {
                            val message = jsonResponse.getString("message")
                            onResult(AuthState.Success(message))
                        } else {
                            val errorMessage = jsonResponse?.getString("message") ?: "Signup failed"
                            onResult(AuthState.Error(errorMessage))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Signup error", e)
                withContext(Dispatchers.Main) {
                    onResult(AuthState.Error("Network error: ${e.message}"))
                }
            }
        }
    }

    // Login function
    fun login(email: String, password: String, onResult: (AuthState) -> Unit) {
        onResult(AuthState.Loading)

        val json = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/login")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && jsonResponse != null) {
                            val message = jsonResponse.getString("message")
                            onResult(AuthState.Success(message))
                        } else {
                            val errorMessage = jsonResponse?.getString("message") ?: "Login failed"
                            onResult(AuthState.Error(errorMessage))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login error", e)
                withContext(Dispatchers.Main) {
                    onResult(AuthState.Error("Network error: ${e.message}"))
                }
            }
        }
    }

    // Email verification with code function
    fun verifyEmailWithCode(email: String, code: String, onResult: (AuthState) -> Unit) {
        onResult(AuthState.Loading)

        val json = JSONObject().apply {
            put("email", email)
            put("code", code)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/verify-email")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && jsonResponse != null) {
                            val message = jsonResponse.getString("message")
                            onResult(AuthState.Success(message))
                        } else {
                            val errorMessage = jsonResponse?.getString("message") ?: "Verification failed"
                            onResult(AuthState.Error(errorMessage))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Verification error", e)
                withContext(Dispatchers.Main) {
                    onResult(AuthState.Error("Network error: ${e.message}"))
                }
            }
        }
    }

    // Resend verification code function
    fun resendVerificationCode(email: String, onResult: (AuthState) -> Unit) {
        onResult(AuthState.Loading)

        val json = JSONObject().apply {
            put("email", email)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/resend-verification")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && jsonResponse != null) {
                            val message = jsonResponse.getString("message")
                            onResult(AuthState.Success(message))
                        } else {
                            val errorMessage = jsonResponse?.getString("message") ?: "Failed to resend code"
                            onResult(AuthState.Error(errorMessage))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Resend code error", e)
                withContext(Dispatchers.Main) {
                    onResult(AuthState.Error("Network error: ${e.message}"))
                }
            }
        }
    }

    // Request password reset function
    fun requestPasswordReset(email: String, onResult: (AuthState) -> Unit) {
        onResult(AuthState.Loading)

        val json = JSONObject().apply {
            put("email", email)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/request-reset")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && jsonResponse != null) {
                            val message = jsonResponse.getString("message")
                            onResult(AuthState.PasswordResetRequested(message))
                        } else {
                            val errorMessage = jsonResponse?.getString("message") ?: "Failed to request password reset"
                            onResult(AuthState.Error(errorMessage))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Password reset request error", e)
                withContext(Dispatchers.Main) {
                    onResult(AuthState.Error("Network error: ${e.message}"))
                }
            }
        }
    }

    // Reset password with code function
    fun resetPasswordWithCode(email: String, code: String, newPassword: String, onResult: (AuthState) -> Unit) {
        onResult(AuthState.Loading)

        val json = JSONObject().apply {
            put("email", email)
            put("code", code)
            put("new_password", newPassword)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/reset-password")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && jsonResponse != null) {
                            val message = jsonResponse.getString("message")
                            onResult(AuthState.PasswordResetSuccess(message))
                        } else {
                            val errorMessage = jsonResponse?.getString("message") ?: "Password reset failed"
                            onResult(AuthState.Error(errorMessage))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Password reset error", e)
                withContext(Dispatchers.Main) {
                    onResult(AuthState.Error("Network error: ${e.message}"))
                }
            }
        }
    }

    // Request account deletion function
    fun requestAccountDeletion(email: String, onResult: (AuthState) -> Unit) {
        onResult(AuthState.Loading)

        val json = JSONObject().apply {
            put("email", email)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/request-deletion")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && jsonResponse != null) {
                            val message = jsonResponse.getString("message")
                            onResult(AuthState.Success(message))
                        } else {
                            val errorMessage = jsonResponse?.getString("message") ?: "Failed to request account deletion"
                            onResult(AuthState.Error(errorMessage))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Account deletion request error", e)
                withContext(Dispatchers.Main) {
                    onResult(AuthState.Error("Network error: ${e.message}"))
                }
            }
        }
    }

    // Confirm account deletion function
    fun confirmAccountDeletion(email: String, code: String, onResult: (AuthState) -> Unit) {
        onResult(AuthState.Loading)

        val json = JSONObject().apply {
            put("email", email)
            put("code", code)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$BASE_URL/confirm-deletion")
            .post(requestBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && jsonResponse != null) {
                            val message = jsonResponse.getString("message")
                            onResult(AuthState.Success(message))
                        } else {
                            val errorMessage = jsonResponse?.getString("message") ?: "Account deletion failed"
                            onResult(AuthState.Error(errorMessage))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Account deletion confirmation error", e)
                withContext(Dispatchers.Main) {
                    onResult(AuthState.Error("Network error: ${e.message}"))
                }
            }
        }
    }
}