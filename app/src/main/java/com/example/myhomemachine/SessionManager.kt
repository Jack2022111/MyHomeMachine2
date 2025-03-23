package com.example.myhomemachine

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * SessionManager handles user session data and persistent login functionality
 */
class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "user_session", Context.MODE_PRIVATE
    )

    companion object {
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_REMEMBER_ME = "remember_me"
    }

    /**
     * Create user login session
     */
    fun createLoginSession(userId: String, email: String, rememberMe: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_USER_EMAIL, email)
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe)
        editor.apply()
    }

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Check if "Remember Me" is enabled
     */
    fun isRememberMeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)
    }

    /**
     * Get stored session data
     */
    fun getUserDetails(): HashMap<String, String?> {
        val user = HashMap<String, String?>()
        user[KEY_USER_ID] = sharedPreferences.getString(KEY_USER_ID, null)
        user[KEY_USER_EMAIL] = sharedPreferences.getString(KEY_USER_EMAIL, null)
        return user
    }

    /**
     * Clear session details
     */
    fun logoutUser() {
        val rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)
        val editor = sharedPreferences.edit()

        // If "Remember Me" is enabled, only clear login status but keep credentials
        if (rememberMe) {
            editor.putBoolean(KEY_IS_LOGGED_IN, false)
        } else {
            // Clear all data except "Remember Me" preference
            editor.clear()
        }

        editor.apply()
    }

    /**
     * Clear ALL session details (used for account deletion)
     * This ignores the "Remember Me" setting
     */
    fun deleteUserAccount() {
        Log.d("SessionManager", "Deleting user account and clearing all data")
        val editor = sharedPreferences.edit()
        editor.clear()
        val success = editor.commit()  // Use commit() instead of apply() for immediate execution
        Log.d("SessionManager", "Account data cleared: $success")
    }


}