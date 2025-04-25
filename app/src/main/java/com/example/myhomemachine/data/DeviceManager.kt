package com.example.myhomemachine.data

import androidx.compose.runtime.mutableStateListOf

object DeviceManager {
    private val knownDevices = mutableMapOf<String, MutableList<String>>(
        "Camera" to mutableStateListOf(),
        "Light" to mutableStateListOf(),
        "Plug" to mutableStateListOf(),
        "Sensor" to mutableStateListOf(),
        "other1" to mutableStateListOf(),
        "other2" to mutableStateListOf()
    )

    // Store schedules
    private val _schedules = mutableStateListOf<String>()
    val schedules: List<String> get() = _schedules

    fun addSchedule(schedule: String) {
        _schedules.add(schedule)
    }

    // Public accessors for each device type
    val knownCameras: List<String> get() = knownDevices["Camera"] ?: emptyList()
    val knownLights: List<String> get() = knownDevices["Light"] ?: emptyList()
    val knownPlugs: List<String> get() = knownDevices["Plug"] ?: emptyList()
    val knownSensors: List<String> get() = knownDevices["Sensor"] ?: emptyList()

    // Function to add a device with a specific type
    fun addDevice(deviceName: String, type: String) {
        // Get the correct list based on type, ignoring case
        val deviceList = knownDevices[type] ?: return

        // Add the device if it's not already in the list
        if (deviceName !in deviceList) {
            deviceList.add(deviceName)
        }
    }

    // NEW FUNCTION: Remove a device with a specific type
    fun removeDevice(deviceName: String, type: String) {
        // Get the correct list based on type
        val deviceList = knownDevices[type] ?: return

        // Remove the device from the list
        deviceList.remove(deviceName)
    }

    // NEW FUNCTION: Update all schedules
    fun updateSchedules(newSchedules: List<String>) {
        _schedules.clear()
        _schedules.addAll(newSchedules)
    }

    // NEW FUNCTION: Update all device types at once
    fun updateAllDevices(
        lights: List<String>,
        plugs: List<String>,
        cameras: List<String>,
        sensors: List<String>,
        newSchedules: List<String>
    ) {
        knownDevices["Light"]?.clear()
        knownDevices["Light"]?.addAll(lights)

        knownDevices["Plug"]?.clear()
        knownDevices["Plug"]?.addAll(plugs)

        knownDevices["Camera"]?.clear()
        knownDevices["Camera"]?.addAll(cameras)

        knownDevices["Sensor"]?.clear()
        knownDevices["Sensor"]?.addAll(sensors)

        _schedules.clear()
        _schedules.addAll(newSchedules)
    }

    // NEW FUNCTION: Clear all devices
    fun clearAllDevices() {
        knownDevices.values.forEach { it.clear() }
        _schedules.clear()
    }

    // NEW FUNCTION: Remove a schedule
    fun removeSchedule(schedule: String) {
        _schedules.remove(schedule)
    }
}