package com.example.myhomemachine

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LightController {
    private const val LIFX_API_TOKEN = "Bearer c30381e0c360262972348a08fdda96e118d69ded53ec34bd1e06c24bd37fc247"
    private const val LIFX_SELECTOR = "all"
    private var lastColor = "hue:0 saturation:0 brightness:1"
    private var currentBrightness = 0.8f

    fun turnLightOn(context: Context) {
        val apiService = RetrofitClient.instance
        val body = LightState(power = "on", brightness = currentBrightness, color = lastColor)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                apiService.setLightState(
                    selector = LIFX_SELECTOR,
                    authHeader = LIFX_API_TOKEN,
                    body = body
                )
                Log.d("GeofenceAction", "LIFX Light turned ON")
            } catch (e: Exception) {
                Log.e("GeofenceAction", "Failed to turn light on", e)
            }
        }
    }

    fun turnLightOff(context: Context) {
        val apiService = RetrofitClient.instance
        val body = LightState(power = "off", color = lastColor)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                apiService.setLightState(
                    selector = LIFX_SELECTOR,
                    authHeader = LIFX_API_TOKEN,
                    body = body
                )
                Log.d("GeofenceAction", "LIFX Light turned OFF")
            } catch (e: Exception) {
                Log.e("GeofenceAction", "Failed to turn light off", e)
            }
        }
    }
}
