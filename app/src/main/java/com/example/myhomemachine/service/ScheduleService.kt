package com.example.myhomemachine.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myhomemachine.MainActivity
import com.example.myhomemachine.R
import com.example.myhomemachine.data.DeviceManager
import com.example.myhomemachine.DeviceController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class ScheduleService : Service() {
    private val TAG = "ScheduleService"
    private val NOTIFICATION_ID = 1001
    private val CHANNEL_ID = "schedule_service_channel"

    private val timer = Timer()

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Schedule service created")

        // Create notification channel for foreground service
        createNotificationChannel()

        // Start as foreground service
        val notification = createNotification("MyHomeMachine Scheduler", "Monitoring your device schedules")
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Schedule service started")

        // Compute initial delay so the task runs exactly at the top of each minute
        val now = System.currentTimeMillis()
        val nextMinute = ((now / 60000) + 1) * 60000
        val initialDelay = nextMinute - now

        // Set a timer to check schedules every minute, aligned to the minute boundary
        timer.schedule(object : TimerTask() {
            override fun run() {
                checkAndExecuteSchedules()
            }
        }, initialDelay, 60 * 1000) // Check every minute

        return START_STICKY
    }

    private fun checkAndExecuteSchedules() {
        try {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR)
            val minute = calendar.get(Calendar.MINUTE)
            val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
            val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY -> "SUN"
                Calendar.MONDAY -> "MON"
                Calendar.TUESDAY -> "TUE"
                Calendar.WEDNESDAY -> "WED"
                Calendar.THURSDAY -> "THU"
                Calendar.FRIDAY -> "FRI"
                Calendar.SATURDAY -> "SAT"
                else -> ""
            }

            val currentTimeStr = String.format(Locale.getDefault(), "%d:%02d %s", hour, minute, amPm)
            Log.d(TAG, "Checking schedules at $currentTimeStr on $dayOfWeek")

            // Check each schedule
            DeviceManager.schedules.forEach { scheduleStr ->
                // Extract time and day pattern from schedule string
                if (isScheduleMatchingCurrentTime(scheduleStr, currentTimeStr, dayOfWeek)) {
                    executeSchedule(scheduleStr)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking schedules: ${e.message}")
        }
    }

    private fun isScheduleMatchingCurrentTime(scheduleStr: String, currentTimeStr: String, currentDay: String): Boolean {
        // Simple example matching logic - adjust based on your schedule string format
        // This example assumes schedule strings contain both time and day
        val containsTime = scheduleStr.contains(currentTimeStr)
        val containsDay = scheduleStr.contains(currentDay) || scheduleStr.contains("Every day") || scheduleStr.contains("Everyday")

        return containsTime && containsDay
    }

    private fun executeSchedule(schedule: String) {
        Log.d(TAG, "Executing schedule: $schedule")

        try {
            // Parse the device type from the schedule string
            val deviceType = when {
                schedule.contains("(Light)")  -> "Light"
                schedule.contains("(Plug)")   -> "Plug"
                schedule.contains("(Camera)") -> "Camera"
                schedule.contains("(Sensor)") -> "Sensor"
                else                          -> "Unknown"
            }

            // Parse the action from the schedule string
            val action = when {
                schedule.contains("turn ON")  -> "ON"
                schedule.contains("turn OFF") -> "OFF"
                else                           -> "Unknown"
            }

            // Create an intent to broadcast the schedule execution
            val intent = Intent("com.example.myhomemachine.EXECUTE_SCHEDULE")
            intent.putExtra("deviceType", deviceType)
            intent.putExtra("action", action)

            // Send broadcast to MainActivity
            sendBroadcast(intent)

            // Show notification
            val notification = createNotification(
                "Schedule Executed",
                "Executed: $schedule"
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(schedule.hashCode(), notification)

            // Control the device directly via your DeviceController
            val controller = DeviceController(this)
            when (deviceType) {
                "Light" -> {
                    if (action == "ON") {
                        CoroutineScope(Dispatchers.IO).launch {
                            controller.turnOnLight()
                        }
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            controller.turnOffLight()
                        }
                    }
                }
                "Plug" -> {
                    if (action == "ON") controller.turnOnPlug() else controller.turnOffPlug()
                }
                // Add other device types as needed
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error executing schedule: ${e.message}")
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Schedule Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for scheduler service notifications"
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(title: String, content: String): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        timer.cancel()
        Log.d(TAG, "Schedule service destroyed")
        super.onDestroy()
    }
}
