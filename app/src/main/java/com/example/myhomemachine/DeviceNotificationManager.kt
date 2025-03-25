package com.example.myhomemachine

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class DeviceNotificationManager(private val context: Context) {
    companion object {
        private const val CHANNEL_ID = "DeviceNotificationChannel"
        private const val CHANNEL_NAME = "Device Notifications"
        private const val NOTIFICATION_ID = 1
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for connected devices and system events"
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendDeviceNotification(
        deviceType: DeviceType,
        eventType: EventType,
        deviceName: String,
        additionalDetails: String? = null
    ) {
        val title = when (eventType) {
            EventType.CONNECTED -> "New ${deviceType.name} Connected"
            EventType.DISCONNECTED -> "${deviceType.name} Disconnected"
            EventType.MOTION_DETECTED -> "Motion Detected by ${deviceType.name}"
            EventType.STATUS_CHANGE -> "${deviceType.name} Status Changed"
        }

        val content = when (eventType) {
            EventType.CONNECTED -> "$deviceName has been connected"
            EventType.DISCONNECTED -> "$deviceName has been disconnected"
            EventType.MOTION_DETECTED -> "Motion detected by $deviceName"
            EventType.STATUS_CHANGE -> "$deviceName has changed its status"
        } + (additionalDetails?.let { " - $it" } ?: "")

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
        notificationManager?.notify(NOTIFICATION_ID, builder.build())
    }
}
