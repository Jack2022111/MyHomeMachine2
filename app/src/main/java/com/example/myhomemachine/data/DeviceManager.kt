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
    val knownOther1: List<String> get() = knownDevices["other1"] ?: emptyList()
    val knownOther2: List<String> get() = knownDevices["other2"] ?: emptyList()

    // Function to add a device with a specific type
    fun addDevice(deviceName: String, type: String) {
        // Get the correct list based on type, ignoring case
        val deviceList = knownDevices[type] ?: return

        // Add the device if it's not already in the list
        if (deviceName !in deviceList) {
            deviceList.add(deviceName)
        }
    }


}