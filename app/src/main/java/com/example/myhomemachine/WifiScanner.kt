package com.example.myhomemachine

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import android.net.wifi.WifiNetworkSpecifier
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import android.content.Intent
import android.util.Log
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri

//class WifiScanner(private val activity: FragmentActivity) {

//class WifiScanner(
//    private val activity: FragmentActivity,
//    private val onScanResults: (List<String>) -> Unit // Callback to update UI with scan results
//) {

class WifiScanner(
    private val activity: FragmentActivity,
    private val logCallback: (String) -> Unit // Callback for logging messages
) {
    private val wifiManager: WifiManager =
        activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.CHANGE_NETWORK_STATE
    )

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermissionsAndScan() {
        logCallback("Checking permissions and starting scan...")
        val missingPermissions = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            logCallback("Missing permissions: ${missingPermissions.joinToString()}")
            ActivityCompat.requestPermissions(activity, missingPermissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else if (!isLocationEnabled()) {
            logCallback("Location services not enabled. Prompting user to enable.")
            activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            checkAndRequestWriteSettingsPermission()
            logCallback("Permissions granted. Starting Wi-Fi scan.")
            startWifiScan()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkAndRequestWriteSettingsPermission() {
        if (!Settings.System.canWrite(activity)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = Uri.parse("package:${activity.packageName}")
            }
            activity.startActivity(intent)
            logCallback("Requested WRITE_SETTINGS permission. Please grant it in the settings screen.")
        } else {
            logCallback("WRITE_SETTINGS permission already granted.")
        }
    }


    private fun startWifiScan() {
        try {
            logCallback("Attempting to start Wi-Fi scan.")
            if (wifiManager.startScan()) {
                val scanResults = wifiManager.scanResults
                logCallback("Wi-Fi scan successful. Found ${scanResults.size} networks.")
                scanResults.forEach { scanResult ->
                    logCallback("Found network: ${scanResult.SSID}")
                }

                val shellyDevice = findShellyDevice(scanResults)
                shellyDevice?.let {
                    logCallback("Found Shelly device with SSID: ${it.SSID}")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        connectToShellyDevice(it.SSID)
                    } else {
                        logCallback("Wi-Fi connection requires Android 10 (API 29) or higher.")
                    }
                } ?: run {
                    logCallback("No Shelly device found.")
                }
            } else {
                logCallback("Wi-Fi scan failed.")
            }
        } catch (e: SecurityException) {
            logCallback("Permission error during Wi-Fi scan: ${e.message}")
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun findShellyDevice(scanResults: List<ScanResult>): ScanResult? {
        return scanResults.find { it.SSID.startsWith("S") }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun connectToShellyDevice(ssid: String) {
        logCallback("Connecting to Shelly device with SSID: $ssid")

        val specifier = WifiNetworkSpecifier.Builder()
            .setSsid(ssid)
            .build()

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(specifier)
            .build()

        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectivityManager.bindProcessToNetwork(network)
                logCallback("Successfully connected to Shelly device with SSID: $ssid")
            }

            override fun onUnavailable() {
                logCallback("Failed to connect to Shelly device with SSID: $ssid")
            }
        })
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}

