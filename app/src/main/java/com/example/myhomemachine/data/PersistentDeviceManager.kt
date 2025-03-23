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
        private const val KEY_LIGHTS = "lights"
        private const val KEY_PLUGS = "plugs"
        private const val KEY_CAMERAS = "cameras"
        private const val KEY_SENSORS = "sensors"
        private const val KEY_SCHEDULES = "schedules"
        private const val KEY_USER_EMAIL = "user_email"

        // Static access to existing device lists from the original DeviceManager
        // for backward compatibility during the transition
        fun migrateDevices(context: Context) {
            val persistentDeviceManager = PersistentDeviceManager(context)
            persistentDeviceManager.saveDevices(
                DeviceManager.knownLights,
                DeviceManager.knownPlugs,
                DeviceManager.knownCameras,
                DeviceManager.knownSensors,
                DeviceManager.schedules
            )
        }
    }

    /**
     * Save devices for the current user
     */
    fun saveDevices(
        lights: List<String>,
        plugs: List<String>,
        cameras: List<String>,
        sensors: List<String>,
        schedules: List<String>
    ) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_LIGHTS, gson.toJson(lights))
        editor.putString(KEY_PLUGS, gson.toJson(plugs))
        editor.putString(KEY_CAMERAS, gson.toJson(cameras))
        editor.putString(KEY_SENSORS, gson.toJson(sensors))
        editor.putString(KEY_SCHEDULES, gson.toJson(schedules))
        editor.apply()
    }

    /**
     * Save devices specifically for a user (for multi-user support)
     */
    fun saveDevicesForUser(
        userEmail: String,
        lights: List<String>,
        plugs: List<String>,
        cameras: List<String>,
        sensors: List<String>,
        schedules: List<String>
    ) {
        val editor = sharedPreferences.edit()

        // Store the current user email
        editor.putString(KEY_USER_EMAIL, userEmail)

        // Store devices for the user
        saveDevices(lights, plugs, cameras, sensors, schedules)
        editor.apply()
    }

    /**
     * Load devices for the current user
     */
    fun loadDevices() {
        // Load devices from SharedPreferences
        val typeToken = object : TypeToken<List<String>>() {}.type

        val lightsJson = sharedPreferences.getString(KEY_LIGHTS, "[]") ?: "[]"
        val plugsJson = sharedPreferences.getString(KEY_PLUGS, "[]") ?: "[]"
        val camerasJson = sharedPreferences.getString(KEY_CAMERAS, "[]") ?: "[]"
        val sensorsJson = sharedPreferences.getString(KEY_SENSORS, "[]") ?: "[]"
        val schedulesJson = sharedPreferences.getString(KEY_SCHEDULES, "[]") ?: "[]"

        // Parse the JSON data
        val lights: List<String> = gson.fromJson(lightsJson, typeToken)
        val plugs: List<String> = gson.fromJson(plugsJson, typeToken)
        val cameras: List<String> = gson.fromJson(camerasJson, typeToken)
        val sensors: List<String> = gson.fromJson(sensorsJson, typeToken)
        val schedules: List<String> = gson.fromJson(schedulesJson, typeToken)

        // Update the static DeviceManager using the new updateAllDevices method
        DeviceManager.updateAllDevices(lights, plugs, cameras, sensors, schedules)
    }

    /**
     * Add a new device and save to persistent storage
     */
    fun addDevice(name: String, type: String) {
        // Add the device to the DeviceManager
        DeviceManager.addDevice(name, type)

        // Save the updated device lists to persistent storage
        saveDevices(
            DeviceManager.knownLights,
            DeviceManager.knownPlugs,
            DeviceManager.knownCameras,
            DeviceManager.knownSensors,
            DeviceManager.schedules
        )
    }

    /**
     * Add a schedule and save to persistent storage
     */
    fun addSchedule(schedule: String) {
        // Add the schedule to the DeviceManager
        DeviceManager.addSchedule(schedule)

        // Save the updated schedules to persistent storage
        saveDevices(
            DeviceManager.knownLights,
            DeviceManager.knownPlugs,
            DeviceManager.knownCameras,
            DeviceManager.knownSensors,
            DeviceManager.schedules
        )
    }

    /**
     * Clear all devices for the current user
     */
    fun clearDevices() {
        // Clear the SharedPreferences
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Clear the DeviceManager
        DeviceManager.clearAllDevices()
    }
    /**
     * Remove a device and update persistent storage
     */
    fun removeDevice(name: String, type: String) {
        // Remove the device from the DeviceManager
        DeviceManager.removeDevice(name, type)

        // Save the updated device lists to persistent storage
        saveDevices(
            DeviceManager.knownLights,
            DeviceManager.knownPlugs,
            DeviceManager.knownCameras,
            DeviceManager.knownSensors,
            DeviceManager.schedules
        )
    }

    /**
     * Remove a schedule and update persistent storage
     */
    fun removeSchedule(schedule: String) {
        // Remove the schedule from the DeviceManager
        DeviceManager.removeSchedule(schedule)

        // Save the updated schedules to persistent storage
        saveDevices(
            DeviceManager.knownLights,
            DeviceManager.knownPlugs,
            DeviceManager.knownCameras,
            DeviceManager.knownSensors,
            DeviceManager.schedules
        )
    }
}