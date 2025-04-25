package com.example.myhomemachine.data

data class DeviceUsageEvent(
    val deviceId: String,
    val deviceName: String,
    val deviceType: String,  // "Light", "Plug", etc.
    val action: String,      // "ON", "OFF", "BRIGHTNESS", etc.
    val value: String = "",  // For brightness levels, colors, etc.
    val timestamp: Long = System.currentTimeMillis()
)

data class AutomationSuggestion(
    val id: String,
    val deviceId: String,
    val deviceName: String,
    val deviceType: String,
    val action: String,
    val value: String = "",
    val timeOfDay: String,   // e.g., "07:00"
    val daysOfWeek: List<String>, // e.g., ["MON", "TUE", "WED", "THU", "FRI"]
    val confidence: Float,   // 0.0 to 1.0
    val description: String  // User-friendly description
)