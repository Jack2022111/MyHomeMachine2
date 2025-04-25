package com.example.myhomemachine.data

import java.time.DayOfWeek
import java.time.LocalTime
import java.util.Locale
import java.util.UUID

data class Schedule(
    val id: String = UUID.randomUUID().toString(),
    val deviceName: String,
    val deviceType: String,
    val action: String, // e.g., "ON", "OFF", "SET_COLOR", "SET_BRIGHTNESS"
    val value: String = "", // For actions like SET_COLOR or SET_BRIGHTNESS
    val time: LocalTime,
    val days: Set<DayOfWeek>,
    val isEnabled: Boolean = true
) {
    override fun toString(): String {
        val daysString = if (days.size == 7) {
            "Every day"
        } else {
            days.joinToString(", ") { it.toString().substring(0, 3) }
        }

        val actionString = when (action) {
            "ON" -> "turn ON"
            "OFF" -> "turn OFF"
            "SET_COLOR" -> "set color to $value"
            "SET_BRIGHTNESS" -> "set brightness to $value%"
            else -> action
        }

        return "$deviceName ($deviceType): $actionString at ${formatTime(time)} on $daysString"
    }

    private fun formatTime(time: LocalTime): String {
        val hour = time.hour
        val minute = time.minute
        val amPm = if (hour < 12) "AM" else "PM"
        val hour12 = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        return String.format(Locale.getDefault(), "%d:%02d %s", hour12, minute, amPm)
    }
}