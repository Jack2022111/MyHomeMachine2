package com.example.myhomemachine.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

class UsageTrackingManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("usage_data", Context.MODE_PRIVATE)
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val BASE_URL = "http://10.0.2.2:5000/api"  // Change to your server address

    companion object {
        private const val KEY_PENDING_EVENTS = "pending_events"
        private const val KEY_SUGGESTIONS = "automation_suggestions"
        private const val TAG = "UsageTrackingManager"
    }

    // Track device usage event
    fun trackDeviceUsage(deviceName: String, deviceType: String, action: String, value: String = "") {
        val event = DeviceUsageEvent(
            deviceId = "${deviceType}_${deviceName}".replace(" ", "_").lowercase(),
            deviceName = deviceName,
            deviceType = deviceType,
            action = action,
            value = value
        )

        // Store event locally
        addPendingEvent(event)

        // Send to server
        sendPendingEvents()
    }

    // Add event to local storage
    private fun addPendingEvent(event: DeviceUsageEvent) {
        val pendingEvents = getPendingEvents().toMutableList()
        pendingEvents.add(event)

        val editor = sharedPreferences.edit()
        editor.putString(KEY_PENDING_EVENTS, gson.toJson(pendingEvents))
        editor.apply()

        Log.d(TAG, "Added usage event: $event")
    }

    // Get pending events from local storage
    private fun getPendingEvents(): List<DeviceUsageEvent> {
        val json = sharedPreferences.getString(KEY_PENDING_EVENTS, "[]") ?: "[]"
        val type = object : TypeToken<List<DeviceUsageEvent>>() {}.type
        return gson.fromJson(json, type)
    }

    // Clear pending events
    private fun clearPendingEvents() {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PENDING_EVENTS, "[]")
        editor.apply()
    }

    // Send pending events to server
    fun sendPendingEvents() {
        val events = getPendingEvents()
        if (events.isEmpty()) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val json = JSONObject().apply {
                    put("events", JSONObject(gson.toJson(events)))
                }

                val requestBody = json.toString().toRequestBody("application/json".toMediaType())
                val request = Request.Builder()
                    .url("$BASE_URL/usage/track")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        Log.d(TAG, "Successfully sent ${events.size} events to server")
                        clearPendingEvents()
                    } else {
                        Log.e(TAG, "Failed to send events: ${response.code}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending events: ${e.message}")
            }
        }
    }

    // Fetch automation suggestions from server
    fun fetchSuggestions(callback: (List<AutomationSuggestion>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder()
                    .url("$BASE_URL/usage/suggestions")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val jsonResponse = JSONObject(response.body?.string() ?: "{}")
                        // Fixed the if expression by adding an else branch
                        val suggestions = if (jsonResponse.optBoolean("success", false)) {
                            val suggestionsJson = jsonResponse.optJSONArray("suggestions")?.toString() ?: "[]"
                            val type = object : TypeToken<List<AutomationSuggestion>>() {}.type
                            gson.fromJson<List<AutomationSuggestion>>(suggestionsJson, type)
                        } else {
                            emptyList()
                        }

                        // Save suggestions locally
                        saveSuggestions(suggestions)

                        // Return suggestions via callback
                        CoroutineScope(Dispatchers.Main).launch {
                            callback(suggestions)
                        }
                    } else {
                        Log.e(TAG, "Failed to fetch suggestions: ${response.code}")
                        // Return empty list on failure
                        CoroutineScope(Dispatchers.Main).launch {
                            callback(emptyList())
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching suggestions: ${e.message}")
                // Return empty list on error
                CoroutineScope(Dispatchers.Main).launch {
                    callback(emptyList())
                }
            }
        }
    }

    // Save suggestions to local storage
    private fun saveSuggestions(suggestions: List<AutomationSuggestion>) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_SUGGESTIONS, gson.toJson(suggestions))
        editor.apply()
    }

    // Get locally saved suggestions
    fun getSavedSuggestions(): List<AutomationSuggestion> {
        val json = sharedPreferences.getString(KEY_SUGGESTIONS, "[]") ?: "[]"
        val type = object : TypeToken<List<AutomationSuggestion>>() {}.type
        return gson.fromJson(json, type)
    }

    // Accept a suggestion and create a schedule
    fun acceptSuggestion(suggestion: AutomationSuggestion, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val json = JSONObject().apply {
                    put("suggestionId", suggestion.id)
                    put("accepted", true)
                }

                val requestBody = json.toString().toRequestBody("application/json".toMediaType())
                val request = Request.Builder()
                    .url("$BASE_URL/usage/suggestion/response")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    val success = response.isSuccessful

                    if (success) {
                        // Create a schedule based on the suggestion
                        createScheduleFromSuggestion(suggestion)
                    }

                    CoroutineScope(Dispatchers.Main).launch {
                        callback(success)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error accepting suggestion: ${e.message}")
                CoroutineScope(Dispatchers.Main).launch {
                    callback(false)
                }
            }
        }
    }

    // Reject a suggestion
    fun rejectSuggestion(suggestion: AutomationSuggestion, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val json = JSONObject().apply {
                    put("suggestionId", suggestion.id)
                    put("accepted", false)
                }

                val requestBody = json.toString().toRequestBody("application/json".toMediaType())
                val request = Request.Builder()
                    .url("$BASE_URL/usage/suggestion/response")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    CoroutineScope(Dispatchers.Main).launch {
                        callback(response.isSuccessful)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error rejecting suggestion: ${e.message}")
                CoroutineScope(Dispatchers.Main).launch {
                    callback(false)
                }
            }
        }
    }

    // Create a schedule from a suggestion
    private fun createScheduleFromSuggestion(suggestion: AutomationSuggestion) {
        val deviceManager = PersistentDeviceManager(context)

        // Format the days string
        val daysString = if (suggestion.daysOfWeek.size == 7) {
            "Every day"
        } else {
            suggestion.daysOfWeek.joinToString(", ")
        }

        // Format the action string
        val actionString = when (suggestion.action) {
            "ON" -> "turn ON"
            "OFF" -> "turn OFF"
            "SET_COLOR" -> "set color to ${suggestion.value}"
            "SET_BRIGHTNESS" -> "set brightness to ${suggestion.value}%"
            else -> suggestion.action
        }


        // Create the schedule string
        val scheduleString = "${suggestion.deviceName} (${suggestion.deviceType}): " +
                "$actionString at ${suggestion.timeOfDay} on $daysString"

        // Save the schedule
        deviceManager.saveSchedule(scheduleString)
        Log.d(TAG, "Created schedule: $scheduleString")
    }
}