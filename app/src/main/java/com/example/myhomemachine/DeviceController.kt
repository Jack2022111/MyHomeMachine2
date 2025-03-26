import android.content.Context
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

import com.example.myhomemachine.DeviceNotificationManager
import com.example.myhomemachine.DeviceType
import com.example.myhomemachine.EventType
open class DeviceController(private val context: Context) {
    open val client = OkHttpClient()
    open val deviceNotifier = DeviceNotificationManager(context)

    // Function to turn on the Shelly Plug
    fun turnOnPlug() {
        toggleShellyRelay(true)
        deviceNotifier.sendDeviceNotification(
            deviceType = DeviceType.PLUG,
            eventType = EventType.STATUS_CHANGE,
            deviceName = "Shelly Plug",
            additionalDetails = "Plug turned ON"
        )
    }

    // Turn off the Shelly Plug
    open fun turnOffPlug() {
        toggleShellyRelay(false)
        deviceNotifier.sendDeviceNotification(
            deviceType = DeviceType.PLUG,
            eventType = EventType.STATUS_CHANGE,
            deviceName = "Shelly Plug",
            additionalDetails = "Plug turned OFF"
        )
    }

    // Send the request and log the response
    private fun toggleShellyRelay(turnOn: Boolean) {
        val url = if (turnOn) "http://192.168.33.1/relay/0?turn=on" else "http://192.168.33.1/relay/0?turn=off"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ShellyControl", "Failed to toggle Shelly plug: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("ShellyControl", "Shelly plug toggled successfully.")
                } else {
                    Log.e("ShellyControl", "Error toggling Shelly plug: ${response.code}")
                }
            }
        })
    }
}