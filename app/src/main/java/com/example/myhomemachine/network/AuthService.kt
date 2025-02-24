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
        data class Success(
            val userId: String = "",
            val email: String = "",
            val message: String = ""
        ) : AuthResult()
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
                        403 -> AuthResult.Error("Please verify your email before logging in.")
                        400, 401, 404, 409, 500, 503 -> handleCommonErrors(response)
                        else -> AuthResult.Error("Login failed: Unknown error (${response.code})")
                    }
                }
            } catch (e: Exception) {
                handleException(e)
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
                                    message = "Please check your email for verification code."
                                )
                            } else {
                                AuthResult.Error(jsonResponse.getString("message"))
                            }
                        }
                        400, 409, 422, 500, 503 -> handleCommonErrors(response)
                        else -> AuthResult.Error("Signup failed: Unknown error (${response.code})")
                    }
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    suspend fun verifyEmailWithCode(email: String, code: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("email", email)
                    put("code", code)
                }

                val requestBody = json.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder()
                    .url("$baseUrl/api/auth/verify-email")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    when (response.code) {
                        200 -> {
                            val responseBody = response.body?.string()
                            val jsonResponse = JSONObject(responseBody)
                            if (jsonResponse.getBoolean("success")) {
                                AuthResult.Success(
                                    message = "Email has been successfully verified."
                                )
                            } else {
                                AuthResult.Error(jsonResponse.getString("message"))
                            }
                        }
                        400 -> AuthResult.Error("Invalid verification code.")
                        500, 503 -> handleCommonErrors(response)
                        else -> AuthResult.Error("Email verification failed: Unknown error (${response.code})")
                    }
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    suspend fun resendVerificationCode(email: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("email", email)
                }

                val requestBody = json.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder()
                    .url("$baseUrl/api/auth/resend-verification")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    when (response.code) {
                        200 -> {
                            val responseBody = response.body?.string()
                            val jsonResponse = JSONObject(responseBody)
                            if (jsonResponse.getBoolean("success")) {
                                AuthResult.Success(
                                    message = "New verification code sent."
                                )
                            } else {
                                AuthResult.Error(jsonResponse.getString("message"))
                            }
                        }
                        404 -> AuthResult.Error("Email not found or already verified.")
                        400, 500, 503 -> handleCommonErrors(response)
                        else -> AuthResult.Error("Failed to resend code: Unknown error (${response.code})")
                    }
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    suspend fun requestPasswordReset(email: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("email", email)
                }

                val requestBody = json.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder()
                    .url("$baseUrl/api/auth/request-reset")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    when (response.code) {
                        200 -> {
                            val responseBody = response.body?.string()
                            val jsonResponse = JSONObject(responseBody)
                            if (jsonResponse.getBoolean("success")) {
                                AuthResult.Success(
                                    message = "Password reset code has been sent to your email."
                                )
                            } else {
                                AuthResult.Error(jsonResponse.getString("message"))
                            }
                        }
                        404 -> AuthResult.Error("Email not found.")
                        400, 500, 503 -> handleCommonErrors(response)
                        else -> AuthResult.Error("Password reset request failed: Unknown error (${response.code})")
                    }
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    suspend fun resetPasswordWithCode(email: String, code: String, newPassword: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("email", email)
                    put("code", code)
                    put("new_password", newPassword)
                }

                val requestBody = json.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder()
                    .url("$baseUrl/api/auth/reset-password")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    when (response.code) {
                        200 -> {
                            val responseBody = response.body?.string()
                            val jsonResponse = JSONObject(responseBody)
                            if (jsonResponse.getBoolean("success")) {
                                AuthResult.Success(
                                    message = "Password has been successfully reset."
                                )
                            } else {
                                AuthResult.Error(jsonResponse.getString("message"))
                            }
                        }
                        400 -> AuthResult.Error("Invalid reset code.")
                        500, 503 -> handleCommonErrors(response)
                        else -> AuthResult.Error("Password reset failed: Unknown error (${response.code})")
                    }
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    suspend fun requestAccountDeletion(email: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("email", email)
                }

                val requestBody = json.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder()
                    .url("$baseUrl/api/auth/request-deletion")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    when (response.code) {
                        200 -> {
                            val responseBody = response.body?.string()
                            val jsonResponse = JSONObject(responseBody)
                            if (jsonResponse.getBoolean("success")) {
                                AuthResult.Success(
                                    message = "Account deletion code has been sent to your email."
                                )
                            } else {
                                AuthResult.Error(jsonResponse.getString("message"))
                            }
                        }
                        404 -> AuthResult.Error("Email not found.")
                        400, 500, 503 -> handleCommonErrors(response)
                        else -> AuthResult.Error("Account deletion request failed: Unknown error (${response.code})")
                    }
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    suspend fun confirmAccountDeletion(email: String, code: String): AuthResult {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("email", email)
                    put("code", code)
                }

                val requestBody = json.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder()
                    .url("$baseUrl/api/auth/confirm-deletion")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    when (response.code) {
                        200 -> {
                            val responseBody = response.body?.string()
                            val jsonResponse = JSONObject(responseBody)
                            if (jsonResponse.getBoolean("success")) {
                                AuthResult.Success(
                                    message = "Account has been successfully deleted."
                                )
                            } else {
                                AuthResult.Error(jsonResponse.getString("message"))
                            }
                        }
                        400 -> AuthResult.Error("Invalid deletion code.")
                        500, 503 -> handleCommonErrors(response)
                        else -> AuthResult.Error("Account deletion failed: Unknown error (${response.code})")
                    }
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun handleCommonErrors(response: okhttp3.Response): AuthResult {
        val responseBody = response.body?.string()
        return try {
            val jsonResponse = JSONObject(responseBody)
            AuthResult.Error(jsonResponse.getString("message"))
        } catch (e: Exception) {
            when (response.code) {
                400 -> AuthResult.Error("Invalid request format.")
                401 -> AuthResult.Error("Invalid credentials.")
                409 -> AuthResult.Error("Email already registered.")
                422 -> AuthResult.Error("Invalid input format.")
                500 -> AuthResult.Error("Server error. Please try again later.")
                503 -> AuthResult.Error("Service temporarily unavailable.")
                else -> AuthResult.Error("Operation failed: Unknown error (${response.code})")
            }
        }
    }

    private fun handleException(e: Exception): AuthResult {
        return when (e) {
            is IOException -> AuthResult.Error("Network error: Please check your internet connection.")
            else -> AuthResult.Error("An unexpected error occurred. Please try again.")
        }
    }
}