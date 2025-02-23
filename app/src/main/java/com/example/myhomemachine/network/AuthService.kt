package com.example.myhomemachine.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class AuthService {
    private val client = OkHttpClient()
    private val baseUrl = "http://10.0.2.2:5000" // For Android emulator
    // private val baseUrl = "http://YOUR_COMPUTER_IP:5000"  // For physical device
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    sealed class AuthResult {
        data class Success(val userId: String, val email: String) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }

    suspend fun login(email: String, password: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                }

                val requestBody = json.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder()
                    .url("$baseUrl/api/auth/login")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    when (response.code) {
                        200 -> {
                            val responseBody = response.body?.string()
                            val jsonResponse = JSONObject(responseBody)
                            if (jsonResponse.getBoolean("success")) {
                                val user = jsonResponse.getJSONObject("user")
                                AuthResult.Success(
                                    userId = user.getString("id"),
                                    email = user.getString("email")
                                )
                            } else {
                                AuthResult.Error(jsonResponse.getString("message"))
                            }
                        }
                        400 -> AuthResult.Error("Invalid email or password format.")
                        401 -> AuthResult.Error("Invalid email or password combination.")
                        404 -> AuthResult.Error("Account not found. Please sign up first.")
                        409 -> AuthResult.Error("Email already exists with a different login method.")
                        500 -> AuthResult.Error("Server error. Please try again later.")
                        503 -> AuthResult.Error("Service temporarily unavailable. Please try again later.")
                        else -> AuthResult.Error("Login failed: Unknown error (${response.code})")
                    }
                }
            } catch (e: IOException) {
                AuthResult.Error("Network error: Please check your internet connection.")
            } catch (e: Exception) {
                AuthResult.Error("An unexpected error occurred. Please try again.")
            }
        }
    }

    suspend fun signup(email: String, password: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                }

                val requestBody = json.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder()
                    .url("$baseUrl/api/auth/signup")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    when (response.code) {
                        200, 201 -> {
                            val responseBody = response.body?.string()
                            val jsonResponse = JSONObject(responseBody)
                            if (jsonResponse.getBoolean("success")) {
                                AuthResult.Success(
                                    userId = jsonResponse.optString("id", ""),
                                    email = email
                                )
                            } else {
                                AuthResult.Error(jsonResponse.getString("message"))
                            }
                        }
                        400 -> {
                            val responseBody = response.body?.string()
                            try {
                                val jsonResponse = JSONObject(responseBody)
                                AuthResult.Error(jsonResponse.getString("message"))
                            } catch (e: Exception) {
                                AuthResult.Error("Invalid email or password format.")
                            }
                        }
                        409 -> AuthResult.Error("This email is already registered. Please use a different email or try logging in.")
                        422 -> AuthResult.Error("Invalid input: Please check your email and password format.")
                        500 -> AuthResult.Error("Server error. Please try again later.")
                        503 -> AuthResult.Error("Service temporarily unavailable. Please try again later.")
                        else -> AuthResult.Error("Signup failed: Unknown error (${response.code})")
                    }
                }
            } catch (e: IOException) {
                AuthResult.Error("Network error: Please check your internet connection and try again.")
            } catch (e: Exception) {
                AuthResult.Error("An unexpected error occurred. Please try again.")
            }
        }
    }
}