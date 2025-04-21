package com.example.myhomemachine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent == null) {
            Log.e("GeofenceReceiver", "GeofencingEvent was null!")
            return
        }

        if (geofencingEvent.hasError()) {
            val errorCode = geofencingEvent.errorCode
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(errorCode)
            Log.e("GeofenceReceiver", "Geofence error: $errorMessage ($errorCode)")
            return
        }

        if (intent.action != "com.example.myhomemachine.ACTION_GEOFENCE_EVENT") {
            Log.w("GeofenceReceiver", "Unknown intent action received: ${intent.action}")
            return
        }

        val transitionType = geofencingEvent.geofenceTransition

        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Log.d("GeofenceReceiver", "ENTER geofence 🚪✅")
                Toast.makeText(context, "ENTER geofence", Toast.LENGTH_SHORT).show()
                LightController.turnLightOn(context)
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.d("GeofenceReceiver", "EXIT geofence 🏃‍♂️❌")
                Toast.makeText(context, "EXIT geofence", Toast.LENGTH_SHORT).show()
                LightController.turnLightOff(context)
            }

            else -> {
                Log.d("GeofenceReceiver", "Received geofence broadcast with intent: $intent")
                Log.d("GeofenceReceiver", "Extras: ${intent.extras}")
                Log.w("GeofenceReceiver", "Other transition: $transitionType")
            }
        }
    }
}