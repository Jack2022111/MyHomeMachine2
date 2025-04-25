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
import java.util.concurrent.TimeUnit
import androidx.core.content.edit

// Wrapper for serializing the list of events into a JSON object
data class EventsWrapper(val events: List<DeviceUsageEvent>)

class UsageTrackingManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("usage_data", Context.MODE_PRIVATE)
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val BASE_URL = "http://10.0.2.2:5000/api"

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

        sharedPreferences.edit {
            putString(KEY_PENDING_EVENTS, gson.toJson(pendingEvents))
        }

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
        sharedPreferences.edit {
            putString(KEY_PENDING_EVENTS, "[]")
        }
    }

    // Send pending events to server
    fun sendPendingEvents() {
        val events = getPendingEvents()
        if (events.isEmpty()) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Wrap the list in a JSON object and serialize
                val wrapper = EventsWrapper(events)
                val jsonString = gson.toJson(wrapper)

                val requestBody = jsonString
                    .toRequestBody("application/json".toMediaType())

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
                        val suggestions = if (jsonResponse.optBoolean("success", false)) {
                            val suggestionsJson = jsonResponse.optJSONArray("suggestions")?.toString() ?: "[]"
                            val type = object : TypeToken<List<AutomationSuggestion>>() {}.type
                            gson.fromJson<List<AutomationSuggestion>>(suggestionsJson, type)
                        } else {
                            emptyList()
                        }

                        saveSuggestions(suggestions)
                        CoroutineScope(Dispatchers.Main).launch {
                            callback(suggestions)
                        }
                    } else {
                        Log.e(TAG, "Failed to fetch suggestions: ${response.code}")
                        CoroutineScope(Dispatchers.Main).launch {
                            callback(emptyList())
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching suggestions: ${e.message}")
                CoroutineScope(Dispatchers.Main).launch {
                    callback(emptyList())
                }
            }
        }
    }

    // Save suggestions to local storage
    private fun saveSuggestions(suggestions: List<AutomationSuggestion>) {
        sharedPreferences.edit {
            putString(KEY_SUGGESTIONS, gson.toJson(suggestions))
        }
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

                client.newCall(request).execute().use {
                    CoroutineScope(Dispatchers.Main).launch {
                        callback(it.isSuccessful)
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

        val daysString = if (suggestion.daysOfWeek.size == 7) {
            "Every day"
        } else {
            suggestion.daysOfWeek.joinToString(", ")
        }

        val actionString = when (suggestion.action) {
            "ON" -> "turn ON"
            "OFF" -> "turn OFF"
            "SET_COLOR" -> "set color to ${suggestion.value}"
            "SET_BRIGHTNESS" -> "set brightness to ${suggestion.value}%"
            else -> suggestion.action
        }

        val scheduleString = "${suggestion.deviceName} (${suggestion.deviceType}): " +
                "$actionString at ${suggestion.timeOfDay} on $daysString"

        deviceManager.saveSchedule(scheduleString)
        Log.d(TAG, "Created schedule: $scheduleString")
    }
}
