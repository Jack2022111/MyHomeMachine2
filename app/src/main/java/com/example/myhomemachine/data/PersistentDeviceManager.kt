package com.example.myhomemachine.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * PersistentDeviceManager handles storing and retrieving device information
 * using SharedPreferences for persistence across app sessions
 */
class PersistentDeviceManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREF_NAME, Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        private const val PREF_NAME = "device_prefs"
        private const val KEY_USER_PREFIX = "user_"
        private const val KEY_LIGHTS_SUFFIX = "_lights"
        private const val KEY_PLUGS_SUFFIX = "_plugs"
        private const val KEY_CAMERAS_SUFFIX = "_cameras"
        private const val KEY_SENSORS_SUFFIX = "_sensors"
        private const val KEY_SCHEDULES_SUFFIX = "_schedules"
        private const val KEY_CURRENT_USER = "current_user_id"
    }

    /**
     * Get the current user's email from SharedPreferences
     */
    private fun getCurrentUser(): String {
        return sharedPreferences.getString(KEY_CURRENT_USER, "") ?: ""
    }

    /**
     * Set the current user in SharedPreferences
     */
    private fun setCurrentUser(userEmail: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_CURRENT_USER, userEmail)
        editor.apply()
    }

    /**
     * Generate key for user-specific device storage
     */
    private fun getUserKey(userEmail: String, suffix: String): String {
        return KEY_USER_PREFIX + userEmail.replace("@", "_at_").replace(".", "_dot_") + suffix
    }

    /**
     * Save devices for a specific user
     */
    fun saveDevicesForUser(
        userEmail: String,
        lights: List<String>,
        plugs: List<String>,
        cameras: List<String>,
        sensors: List<String>,
        schedules: List<String>
    ) {
        setCurrentUser(userEmail)

        val editor = sharedPreferences.edit()
        editor.putString(getUserKey(userEmail, KEY_LIGHTS_SUFFIX), gson.toJson(lights))
        editor.putString(getUserKey(userEmail, KEY_PLUGS_SUFFIX), gson.toJson(plugs))
        editor.putString(getUserKey(userEmail, KEY_CAMERAS_SUFFIX), gson.toJson(cameras))
        editor.putString(getUserKey(userEmail, KEY_SENSORS_SUFFIX), gson.toJson(sensors))
        editor.putString(getUserKey(userEmail, KEY_SCHEDULES_SUFFIX), gson.toJson(schedules))
        editor.apply()
    }

    /**
     * Save current devices for the logged-in user
     */
    fun saveCurrentUserDevices() {
        val userEmail = getCurrentUser()
        if (userEmail.isNotEmpty()) {
            saveDevicesForUser(
                userEmail,
                DeviceManager.knownLights,
                DeviceManager.knownPlugs,
                DeviceManager.knownCameras,
                DeviceManager.knownSensors,
                DeviceManager.schedules
            )
        }
    }

    /**
     * Load devices for a specific user
     */
    fun loadDevicesForUser(userEmail: String) {
        setCurrentUser(userEmail)

        // First, clear all existing devices
        DeviceManager.clearAllDevices()

        if (userEmail.isEmpty()) {
            return
        }

        val typeToken = object : TypeToken<List<String>>() {}.type

        // Get user-specific keys
        val lightsKey = getUserKey(userEmail, KEY_LIGHTS_SUFFIX)
        val plugsKey = getUserKey(userEmail, KEY_PLUGS_SUFFIX)
        val camerasKey = getUserKey(userEmail, KEY_CAMERAS_SUFFIX)
        val sensorsKey = getUserKey(userEmail, KEY_SENSORS_SUFFIX)
        val schedulesKey = getUserKey(userEmail, KEY_SCHEDULES_SUFFIX)

        // Load the device lists from SharedPreferences
        val lightsJson = sharedPreferences.getString(lightsKey, "[]") ?: "[]"
        val plugsJson = sharedPreferences.getString(plugsKey, "[]") ?: "[]"
        val camerasJson = sharedPreferences.getString(camerasKey, "[]") ?: "[]"
        val sensorsJson = sharedPreferences.getString(sensorsKey, "[]") ?: "[]"
        val schedulesJson = sharedPreferences.getString(schedulesKey, "[]") ?: "[]"

        // Parse the JSON data
        val lights: List<String> = gson.fromJson(lightsJson, typeToken)
        val plugs: List<String> = gson.fromJson(plugsJson, typeToken)
        val cameras: List<String> = gson.fromJson(camerasJson, typeToken)
        val sensors: List<String> = gson.fromJson(sensorsJson, typeToken)
        val schedules: List<String> = gson.fromJson(schedulesJson, typeToken)

        // Update the DeviceManager with the user's devices
        DeviceManager.updateAllDevices(lights, plugs, cameras, sensors, schedules)
    }

    /**
     * For backward compatibility
     */
    fun loadDevices() {
        val userEmail = getCurrentUser()
        if (userEmail.isNotEmpty()) {
            loadDevicesForUser(userEmail)
        }
    }

    /**
     * Add a device for the current user
     */
    fun addDevice(name: String, type: String) {
        // Add the device to the in-memory DeviceManager
        DeviceManager.addDevice(name, type)

        // Save the updated devices for the current user
        saveCurrentUserDevices()
    }

    /**
     * Remove a device for the current user
     */
    fun removeDevice(name: String, type: String) {
        // Remove the device from the in-memory DeviceManager
        DeviceManager.removeDevice(name, type)

        // Save the updated devices for the current user
        saveCurrentUserDevices()
    }

    /**
     * Add a schedule for the current user
     */
    fun addSchedule(schedule: String) {
        // Add the schedule to the in-memory DeviceManager
        DeviceManager.addSchedule(schedule)

        // Save the updated schedules for the current user
        saveCurrentUserDevices()
    }

    /**
     * Remove a schedule for the current user
     */
    fun removeSchedule(schedule: String) {
        // Remove the schedule from the in-memory DeviceManager
        DeviceManager.removeSchedule(schedule)

        // Save the updated schedules for the current user
        saveCurrentUserDevices()
    }

    /**
     * Clear devices for the current user
     */
    fun clearCurrentUserDevices() {
        DeviceManager.clearAllDevices()
        val userEmail = getCurrentUser()
        setCurrentUser("")
    }
}