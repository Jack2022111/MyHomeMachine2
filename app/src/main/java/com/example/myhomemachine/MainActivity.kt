package com.example.myhomemachine
import androidx.compose.material.icons.filled.AutoAwesome
import android.Manifest.permission
import android.app.Application as AndroidApplication
// for the Notification in the plug
import com.example.myhomemachine.DeviceType
import com.example.myhomemachine.EventType


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.TextField
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SensorsOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.ui.unit.sp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myhomemachine.data.DeviceManager
import com.example.myhomemachine.ui.theme.MyHomeMachineTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.myhomemachine.ui.theme.MyHomeMachineTheme
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.net.wifi.WifiNetworkSpecifier
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import android.content.Intent
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.foundation.border
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider

import kotlin.math.roundToInt
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myhomemachine.network.AuthViewModel
import com.example.myhomemachine.SettingsScreen
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.myhomemachine.ForgotPasswordScreen
import com.example.myhomemachine.data.PersistentDeviceManager
import com.example.myhomemachine.SessionManager


import androidx.navigation.navArgument
import androidx.compose.foundation.text.KeyboardOptions
import com.example.myhomemachine.VerificationType
import com.example.myhomemachine.CodeVerificationScreen
import com.example.myhomemachine.ResetPasswordConfirmScreen
import java.util.UUID
import android.widget.TextView
import com.example.myhomemachine.RetrofitClient
import com.example.myhomemachine.SwitchBotResponse
import com.example.myhomemachine.DeviceStatus
import com.example.myhomemachine.SwitchBotApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.ln
import kotlin.math.exp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.nativeCanvas
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.compose.ui.platform.LocalContext
import android.app.PendingIntent
import androidx.core.app.NotificationManagerCompat
import android.content.BroadcastReceiver

import android.content.IntentFilter
import com.example.myhomemachine.service.ScheduleService
import com.example.myhomemachine.data.Schedule
import java.time.DayOfWeek
import java.time.LocalTime
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.CircleShape
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.example.myhomemachine.data.UsageTrackingManager
import androidx.compose.material3.Badge
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.offset

// lifx stuff super unsecure
private const val LIFX_API_TOKEN = "c30381e0c360262972348a08fdda96e118d69ded53ec34bd1e06c24bd37fc247"
private const val LIFX_SELECTOR = "all" // Can be "label:your_light_name" or "all"

// switchbot therm stuff also super unsecure
private const val BASE_URL = "https://api.switch-bot.com/"
private const val sbdeviceId = "F90D2BD4D12F" // Replace with your actual deviceId
private const val sbtoken = "68bc54e2a5495d4a8f560a38744a2a4178ff857528dd3a84f4eb51b6d75b516070f33a7b823e555f5fdb7d530df79997"
private const val sbsecret = "28f1ee6611fbf2be9cfab357bc7a3480"


fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}


class SharedViewModel(application: Application) : AndroidViewModel(application) {
    val logs = mutableStateListOf<String>()

    // Make wifiScanner non-nullable and initialize properly
    lateinit var wifiScanner: WifiScanner
        private set

    fun initialize(activity: AppCompatActivity) {
        wifiScanner = WifiScanner(activity) { log ->
            logs.add(log)
        }
    }
}

class MainActivity : AppCompatActivity() {
    private var isLightOn = false
    private var lastColor: String = "hue:0 saturation:0 brightness:1" // Default color (White)
    private var currentBrightness: Float = 0.8f // Default brightness (80%)
    private lateinit var deviceController: DeviceController
    lateinit var retrofitClient: RetrofitClient

    private lateinit var usageTrackingManager: UsageTrackingManager
    private val channelId = "i.apps.notifications" // Unique channel ID for notifications
    private val description = "Test notification"  // Description for the notification channel
    private val notificationId = 1234 // Unique identifier for the notification
    private  val CHANNEL_ID = "my_channel_id"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceController = DeviceController(this)
        retrofitClient = RetrofitClient()
        checkAndRequestPermissions()
        createNotificationChannel()

        usageTrackingManager = UsageTrackingManager(this)
        //sendNotification("Welcome to My Home", "Notification is working")

        // Initialize SessionManager and PersistentDeviceManager
        val sessionManager = SessionManager(this)
        val deviceManager = PersistentDeviceManager(this)

        // Load devices from persistent storage if user is logged in
        if (sessionManager.isLoggedIn()) {
            val userDetails = sessionManager.getUserDetails()
            val userEmail = userDetails[SessionManager.KEY_USER_EMAIL] ?: ""
            if (userEmail.isNotEmpty()) {
                deviceManager.loadDevicesForUser(userEmail)
            }
        }

        val sharedViewModel: SharedViewModel by viewModels()
        sharedViewModel.initialize(this)

        setContent {
            MyHomeMachineTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        sharedViewModel = sharedViewModel,
                        onTurnLightOn = { turnLightOn() },
                        onTurnLightOff = { turnLightOff() },
                        onBrightnessChange = { brightness -> setBrightness(brightness) },
                        setBrightness = { brightness -> this.setBrightness(brightness) },
                        onSetColor = { color -> setColor(color) },
                        sessionManager = sessionManager,  // Pass SessionManager to NavHost
                        deviceManager = deviceManager,     // Pass DeviceManager to NavHost
                        deviceController = deviceController
                    )
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // Notification channels are required on Android 8.0+ (API level 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Notification Channel"
            val descriptionText = "This channel is used for demo notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(Title: String, Text: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(Title)
            .setContentText(Text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(this)


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build())
        } else {
            Log.w("Notification", "POST_NOTIFICATIONS permission not granted")
        }
    }


    private val scheduleReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.example.myhomemachine.EXECUTE_SCHEDULE") {
                val deviceType = intent.getStringExtra("deviceType") ?: return
                val action = intent.getStringExtra("action") ?: return

                when (deviceType) {
                    "Light" -> {
                        if (action == "ON") {
                            turnLightOn()
                            sendNotification("Schedule", "Light turned ON")
                        } else if (action == "OFF") {
                            turnLightOff()
                            sendNotification("Schedule", "Light turned OFF")
                        }
                    }
                    "Plug" -> {
                        val deviceController = DeviceController(context)
                        if (action == "ON") {
                            deviceController.turnOnPlug()
                            sendNotification("Schedule", "Plug turned ON")
                        } else if (action == "OFF") {
                            deviceController.turnOffPlug()
                            sendNotification("Schedule", "Plug turned OFF")
                        }
                    }
                    // Add other device types as needed
                }
            }
        }
    }

    override fun onDestroy() {
        try {
            // Unregister broadcast receiver
            unregisterReceiver(scheduleReceiver)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error unregistering receiver: ${e.message}")
        }
        super.onDestroy()
    }

    private fun executeScheduledAction(deviceName: String, deviceType: String, action: String, value: String) {
        when (deviceType) {
            "Light" -> {
                when (action) {
                    "ON" -> turnLightOn()
                    "OFF" -> turnLightOff()
                    "SET_BRIGHTNESS" -> {
                        val brightness = value.toFloatOrNull() ?: 0.8f
                        setBrightness(brightness / 100f) // Convert percentage to 0-1 range
                    }
                    "SET_COLOR" -> {
                        try {
                            // Parse hex color code
                            val colorInt = android.graphics.Color.parseColor(value)
                            val color = Color(colorInt)
                            setColor(color)
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Error parsing color for schedule: $e")
                        }
                    }
                }
                sendNotification("Light Schedule", "Executed $action for $deviceName")
            }
            "Plug" -> {
                val deviceController = DeviceController(this)
                when (action) {
                    "ON" -> deviceController.turnOnPlug()
                    "OFF" -> deviceController.turnOffPlug()
                }
                sendNotification("Plug Schedule", "Executed $action for $deviceName")
            }
            "Camera" -> {
                // Add camera actions here
                sendNotification("Camera Schedule", "Executed $action for $deviceName")
            }
            "Sensor" -> {
                // Add sensor actions here
                sendNotification("Sensor Schedule", "Executed $action for $deviceName")
            }
        }
    }



    // ✅ Fetch Temperature from SwitchBot API
    /*
    private fun fetchTemperature() {
        val timestamp = System.currentTimeMillis().toString()
        val nonce = UUID.randomUUID().toString()
        val sign = generateSignature(sbtoken, sbsecret, timestamp, nonce)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val apiService = retrofit.create(SwitchBotApiService::class.java)

        apiService.getDeviceStatus(sbdeviceId, sbtoken, sign, nonce, timestamp)
            .enqueue(object : retrofit2.Callback<SwitchBotResponse> {
                override fun onResponse(call: retrofit2.Call<SwitchBotResponse>, response: retrofit2.Response<SwitchBotResponse>) {
                    if (response.isSuccessful) {
                        val temp = response.body()?.body?.temperature ?: 0.0
                        runOnUiThread {
                            val temperatureTextView: TextView = findViewById(R.id.temperatureTextView)
                            temperatureTextView.text = "Temperature: $temp °C"
                        }
                        Log.d("SwitchBot", "Temperature: $temp °C")
                    } else {
                        Log.e("SwitchBot", "API Error: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: retrofit2.Call<SwitchBotResponse>, t: Throwable) {
                    Log.e("SwitchBot", "Failed to fetch temperature", t)
                }
            })
    }

     */
    /*
        suspend fun fetchTemperature(): Double? {
            val timestamp = System.currentTimeMillis().toString()
            val nonce = UUID.randomUUID().toString()
            val sign = generateSignature(sbtoken, sbsecret, timestamp, nonce)

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build())
                .build()

            val apiService = retrofit.create(SwitchBotApiService::class.java)

            return try {
                val response = apiService.getDeviceStatus(sbdeviceId, sbtoken, sign, nonce, timestamp).execute()
                if (response.isSuccessful) {
                    response.body()?.body?.temperature?.toDouble() // ✅ Convert Float to Double
                } else {
                    Log.e("SwitchBot", "API Error: ${response.code()} - ${response.message()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("SwitchBot", "Failed to fetch temperature", e)
                null
            }
        }

        private fun generateSignature(token: String, secret: String, timestamp: String, nonce: String): String {
            val data = token + timestamp + nonce
            val mac = javax.crypto.Mac.getInstance("HmacSHA256")
            mac.init(javax.crypto.spec.SecretKeySpec(secret.toByteArray(), "HmacSHA256"))
            return android.util.Base64.encodeToString(mac.doFinal(data.toByteArray()), android.util.Base64.NO_WRAP)
        }

     */


    private fun turnLightOn() {
        val apiService = retrofitClient.instance
        val body = LightState(power = "on", brightness = currentBrightness, color = lastColor)

        lifecycleScope.launch {
            try {
                apiService.setLightState(
                    selector = LIFX_SELECTOR,
                    authHeader = "Bearer $LIFX_API_TOKEN",
                    body = body
                )
                isLightOn = true
                Log.d("LIFX", "Light turned ON with color: $lastColor at brightness: $currentBrightness")
                sendNotification("Light Devices", "Light turned ON with color: $lastColor at brightness: $currentBrightness")

                // Track usage
                usageTrackingManager.trackDeviceUsage(
                    deviceName = "LIFX Smart Light",
                    deviceType = "Light",
                    action = "ON"
                )

            } catch (e: Exception) {
                Log.e("LIFX", "Failed to turn light on", e)
            }
        }
    }

    private fun turnLightOff() {
        val apiService = retrofitClient.instance
        val body = LightState(power = "off", color = lastColor)

        lifecycleScope.launch {
            try {
                apiService.setLightState(
                    selector = LIFX_SELECTOR,
                    authHeader = "Bearer $LIFX_API_TOKEN",
                    body = body
                )
                isLightOn = false
                Log.d("LIFX", "Light turned OFF")
                sendNotification("Light Devices", "Light turned OFF")

                // Track usage
                usageTrackingManager.trackDeviceUsage(
                    deviceName = "LIFX Smart Light",
                    deviceType = "Light",
                    action = "OFF"
                )

            } catch (e: Exception) {
                Log.e("LIFX", "Failed to turn light off", e)
            }
        }
    }

    private fun setBrightness(brightness: Float) {
        currentBrightness = brightness // Store brightness

        val apiService = retrofitClient.instance
        val body = LightState(
            power = "on", // Always keep the light on when changing brightness
            brightness = brightness,
            color = lastColor
        )

        lifecycleScope.launch {
            try {
                apiService.setLightState(
                    selector = LIFX_SELECTOR,
                    authHeader = "Bearer $LIFX_API_TOKEN",
                    body = body
                )
                Log.d("LIFX", "Brightness set to $brightness")

                // Track usage with brightness value
                usageTrackingManager.trackDeviceUsage(
                    deviceName = "LIFX Smart Light",
                    deviceType = "Light",
                    action = "SET_BRIGHTNESS",
                    value = (brightness * 100).toInt().toString()
                )

            } catch (e: Exception) {
                Log.e("LIFX", "Failed to set brightness", e)
            }
        }
    }

    // Function to convert Jetpack Compose Color to HSB
    private fun convertColorToHSB(color: Color): FloatArray {
        val hsv = FloatArray(3)
        android.graphics.Color.RGBToHSV(
            (color.red * 255).toInt(),
            (color.green * 255).toInt(),
            (color.blue * 255).toInt(),
            hsv
        )
        return hsv
    }

    private fun setColor(color: Color) {
        val hsb = convertColorToHSB(color) // Convert Color to HSB
        lastColor = "hue:${hsb[0]} saturation:${hsb[1]} brightness:$currentBrightness"

        val apiService = retrofitClient.instance
        val body = LightState(color = lastColor, power = "on")

        lifecycleScope.launch {
            try {
                apiService.setLightState(
                    selector = LIFX_SELECTOR,
                    authHeader = "Bearer $LIFX_API_TOKEN",
                    body = body
                )
                Log.d("LIFX", "Color set to HSB: ${hsb[0]}, ${hsb[1]}, ${hsb[2]}")

                // Track usage with color value
                usageTrackingManager.trackDeviceUsage(
                    deviceName = "LIFX Smart Light",
                    deviceType = "Light",
                    action = "SET_COLOR",
                    value = "#" + Integer.toHexString(color.toArgb()).substring(2)
                )

            } catch (e: Exception) {
                Log.e("LIFX", "Failed to set color", e)
            }
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        // Check for notification permission on Android 13 (Tiramisu) and higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 2
    }
}

// DeviceController class to manage the Shelly Plug
class DeviceController (private val context: Context) {

    private val client = OkHttpClient()
    private val deviceNotifier = DeviceNotificationManager(context)
    private val usageTrackingManager = UsageTrackingManager(context)
    //private val shellyIpAddress = "http://10.5.2.30" // Shelly plug IP (this is specifically for b2 wifi network it will change later)

    // Function to turn on the Shelly Plug
    fun turnOnPlug() {
        toggleShellyRelay(true)
        deviceNotifier.sendDeviceNotification(
            deviceType = DeviceType.PLUG,
            eventType = EventType.STATUS_CHANGE,
            deviceName = "Shelly Plug",
            additionalDetails = "Plug turned ON"
        )

        // Track usage
        usageTrackingManager.trackDeviceUsage(
            deviceName = "Shelly Plug",
            deviceType = "Plug",
            action = "ON"
        )
    }


    // Turn off the Shelly Plug
    fun turnOffPlug() {
        toggleShellyRelay(false)
        deviceNotifier.sendDeviceNotification(
            deviceType = DeviceType.PLUG,
            eventType = EventType.STATUS_CHANGE,
            deviceName = "Shelly Plug",
            additionalDetails = "Plug turned OFF"
        )

        // Track usage
        usageTrackingManager.trackDeviceUsage(
            deviceName = "Shelly Plug",
            deviceType = "Plug",
            action = "OFF"
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

private fun generateSignature(token: String, secret: String, timestamp: String, nonce: String): String {
    val data = token + timestamp + nonce
    val mac = javax.crypto.Mac.getInstance("HmacSHA256")
    mac.init(javax.crypto.spec.SecretKeySpec(secret.toByteArray(), "HmacSHA256"))
    return android.util.Base64.encodeToString(mac.doFinal(data.toByteArray()), android.util.Base64.NO_WRAP)
}



/*
suspend fun fetchTemperature(): Double? {
    val timestamp = System.currentTimeMillis().toString()
    val nonce = UUID.randomUUID().toString()
    val sign = generateSignature(sbtoken, sbsecret, timestamp, nonce)

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().build())
        .build()

    val apiService = retrofit.create(SwitchBotApiService::class.java)

    return try {
        val response = apiService.getDeviceStatus(sbdeviceId, sbtoken, sign, nonce, timestamp).execute()

        // ✅ Log the full API response to check for issues
        Log.d("SwitchBot", "API Response: ${response.raw()}") // Logs raw HTTP response
        Log.d("SwitchBot", "Response Body: ${response.body()}") // Logs response JSON

        if (response.isSuccessful) {
            val temp = response.body()?.body?.temperature
            if (temp == null) {
                Log.e("SwitchBot", "Temperature is null in response")
                return null
            }
            Log.d("SwitchBot", "Received Temperature: $temp °C")
            return temp.toDouble()
        } else {
            Log.e("SwitchBot", "API Error: ${response.code()} - ${response.message()}")
            null
        }
    } catch (e: Exception) {
        Log.e("SwitchBot", "Failed to fetch temperature", e)
        null
    }
}
*/
/*
suspend fun forceCloudSync() {
    withContext(Dispatchers.IO) {
        val timestamp = System.currentTimeMillis().toString()
        val nonce = UUID.randomUUID().toString()
        val sign = generateSignature(sbtoken, sbsecret, timestamp, nonce)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // e.g., "https://api.switch-bot.com/"
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val apiService = retrofit.create(SwitchBotApiService::class.java)

        try {
            val response = apiService.getDeviceStatus(sbdeviceId, sbtoken, sign, nonce, timestamp).execute()
            Log.d("SwitchBot", "Cloud Sync Response: ${response.body()}")
        } catch (e: Exception) {
            Log.e("SwitchBot", "Failed to sync SwitchBot cloud", e)
        }
    }
}


suspend fun fetchTemperature(): Double? {
    return withContext(Dispatchers.IO) {
        val timestamp = System.currentTimeMillis().toString()
        val nonce = UUID.randomUUID().toString()
        val sign = generateSignature(sbtoken, sbsecret, timestamp, nonce)

        Log.d("SwitchBot", "Sending API Request to: $BASE_URL/v1.1/devices/$sbdeviceId/status")
        Log.d("SwitchBot", "Headers: Authorization=$sbtoken, sign=$sign, nonce=$nonce, t=$timestamp")

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val apiService = retrofit.create(SwitchBotApiService::class.java)

        try {
            val response = apiService.getDeviceStatus(sbdeviceId, sbtoken, sign, nonce, timestamp).execute()
            Log.d("SwitchBot", "API Response: ${response.raw()}")
            Log.d("SwitchBot", "Response Body: ${response.body()}")

            if (response.isSuccessful) {
                val temp = response.body()?.body?.temperature
                Log.d("SwitchBot", "Received Temperature: $temp °C")
                temp?.toDouble()
            } else {
                Log.e("SwitchBot", "API Error: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("SwitchBot", "Failed to fetch temperature", e)
            null
        }
    }
}
*/


suspend fun forceCloudSync() {
    withContext(Dispatchers.IO) {
        val timestamp = System.currentTimeMillis().toString()
        val nonce = UUID.randomUUID().toString()
        val sign = generateSignature(sbtoken, sbsecret, timestamp, nonce)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // e.g., "https://api.switch-bot.com/"
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val apiService = retrofit.create(SwitchBotApiService::class.java)

        try {
            val response = apiService.getDeviceStatus(sbdeviceId, sbtoken, sign, nonce, timestamp).execute()
            Log.d("SwitchBot", "Cloud Sync Response: ${response.body()}")
        } catch (e: Exception) {
            Log.e("SwitchBot", "Failed to sync SwitchBot cloud", e)
        }
    }
}

fun computeHeatIndex(tempC: Double, rh: Double): Double {
    val tempF = tempC * 9 / 5 + 32
    // Constants for the heat index formula (in Fahrenheit)
    val c1 = -42.379
    val c2 = 2.04901523
    val c3 = 10.14333127
    val c4 = -0.22475541
    val c5 = -6.83783e-3
    val c6 = -5.481717e-2
    val c7 = 1.22874e-3
    val c8 = 8.5282e-4
    val c9 = -1.99e-6

    val hiF = c1 + c2 * tempF + c3 * rh + c4 * tempF * rh +
            c5 * tempF * tempF + c6 * rh * rh +
            c7 * tempF * tempF * rh + c8 * tempF * rh * rh +
            c9 * tempF * tempF * rh * rh
    // Convert back to Celsius
    return (hiF - 32) * 5 / 9
}

suspend fun fetchMeterStatus(): MeterStatus? {
    return withContext(Dispatchers.IO) {
        val timestamp = System.currentTimeMillis().toString()
        val nonce = UUID.randomUUID().toString()
        val sign = generateSignature(sbtoken, sbsecret, timestamp, nonce)

        Log.d("SwitchBot", "Sending API Request to: $BASE_URL/v1.1/devices/$sbdeviceId/status")
        Log.d("SwitchBot", "Headers: Authorization=$sbtoken, sign=$sign, nonce=$nonce, t=$timestamp")

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val apiService = retrofit.create(SwitchBotApiService::class.java)

        try {
            val response = apiService.getDeviceStatus(sbdeviceId, sbtoken, sign, nonce, timestamp).execute()
            Log.d("SwitchBot", "API Response: ${response.raw()}")
            Log.d("SwitchBot", "Response Body: ${response.body()}")

            if (response.isSuccessful) {
                // Assuming response.body()?.body returns an object with temperature, humidity, and battery properties.
                val body = response.body()?.body
                val temp = body?.temperature
                val hum = body?.humidity
                val bat = body?.battery
                Log.d("SwitchBot", "Received Temperature: $temp °C, Humidity: $hum %, Battery: $bat %")
                if (temp != null && hum != null && bat != null) {
                    val temperature = temp.toDouble()
                    val humidity = hum.toDouble()

                    // Compute Dew Point using the Magnus-Tetens approximation.
                    val a = 17.27
                    val b = 237.7
                    val gamma = ln(humidity / 100.0) + (a * temperature) / (b + temperature)
                    val dewPoint = (b * gamma) / (a - gamma)

                    // Compute Heat Index (feels-like temperature)
                    val heatIndex = computeHeatIndex(temperature, humidity)

                    // Compute Saturation Vapor Pressure (in hPa)
                    val satVaporPressure = 6.112 * exp(17.67 * temperature / (temperature + 243.5))
                    // Actual Vapor Pressure (in hPa)
                    val vaporPressure = (humidity / 100.0) * satVaporPressure
                    // Absolute Humidity (grams per cubic meter)
                    val absoluteHumidity = (6.112 * exp(17.67 * temperature / (temperature + 243.5)) * humidity * 2.1674) / (273.15 + temperature)
                    // Vapor Pressure Deficit (in hPa)
                    val vaporPressureDeficit = satVaporPressure - vaporPressure
                    // Mixing Ratio (kg water vapor per kg dry air) assuming standard pressure (1013.25 hPa)
                    val mixingRatio = 0.622 * vaporPressure / (1013.25 - vaporPressure)
                    // Enthalpy (approximate, in kJ/kg dry air)
                    val enthalpy = 1.006 * temperature + mixingRatio * (2501 + 1.86 * temperature)

                    MeterStatus(
                        temperature = temperature,
                        humidity = humidity,
                        battery = bat.toInt(),
                        dewPoint = dewPoint,
                        heatIndex = heatIndex,
                        absoluteHumidity = absoluteHumidity,
                        vaporPressure = vaporPressure,
                        saturationVaporPressure = satVaporPressure,
                        vaporPressureDeficit = vaporPressureDeficit,
                        mixingRatio = mixingRatio,
                        enthalpy = enthalpy
                    )
                } else {
                    null
                }
            } else {
                Log.e("SwitchBot", "API Error: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("SwitchBot", "Failed to fetch meter status", e)
            null
        }
    }
}

@Composable
fun FirstScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top section with logo
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo2),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(300.dp)
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Middle section with welcome text
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "MyHomeMachine",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Bottom section with Sign-in/login buttons
            Column(
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .animateContentSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        disabledElevation = 0.dp
                    )

                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Button(
                    onClick = { navController.navigate("signup") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .animateContentSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        disabledElevation = 0.dp
                    )

                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .animateContentSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DEV BYPASS",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Button(
                    onClick = { navController.navigate("wififence") }, // to cracked version of geofence
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .animateContentSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Geofence test",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun WifiMonitorScreen(navController: NavHostController) {
    val context = LocalContext.current
    var connectionStatus by remember { mutableStateOf("Not connected to androidwifi") }
    // Flag to prevent sending repeated notifications
    var notificationSent by remember { mutableStateOf(false) }

    // Helper function to send a notification.
    fun sendNotification(message: String) {
        val channelId = "wifi_notification_channel"
        val notificationManager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "WiFi Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context.applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with actual icon
            .setContentTitle("WiFi Connection Update")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(1, notificationBuilder.build())
    }


    // Periodically check the Wi‑Fi connection status.
    LaunchedEffect(Unit) {
        while (true) {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetwork = connectivityManager?.activeNetworkInfo

            if (activeNetwork != null &&
                activeNetwork.isConnected &&
                activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                val wifiInfo = wifiManager?.connectionInfo
                // Remove any surrounding quotes (some devices return quoted SSIDs)
                val currentSSID = wifiInfo?.ssid?.replace("\"", "")
                if (currentSSID.equals("androidwifi", ignoreCase = true)) {
                    connectionStatus = "Connected to $currentSSID"
                    if (!notificationSent) {
                        sendNotification("Connected to androidwifi")
                        notificationSent = true
                    }
                } else {
                    connectionStatus = "Not connected to androidwifi"
                    notificationSent = false
                }
            } else {
                connectionStatus = "Not connected to androidwifi"
                notificationSent = false
            }
            delay(5000L) // Check every 5 seconds.
        }
    }

    // UI layout
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the current connection status.
            Text(
                text = connectionStatus,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Button to navigate back home.
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Go to Home")
            }
        }
    }
}


/*
// works
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun WifiMonitorScreen(navController: NavHostController) {
    val context = LocalContext.current
    var connectionStatus by remember { mutableStateOf("Not connected to androidwifi") }

    // Periodically check the Wi‑Fi connection status
    LaunchedEffect(Unit) {
        while (true) {
            // Get WifiManager and ConnectivityManager
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetwork = connectivityManager?.activeNetworkInfo

            if (activeNetwork != null &&
                activeNetwork.isConnected &&
                activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                val wifiInfo = wifiManager?.connectionInfo
                // Remove any surrounding quotes from the SSID (some devices return quotes)
                val currentSSID = wifiInfo?.ssid?.replace("\"", "")
                connectionStatus = if (currentSSID.equals("AndroidWifi", ignoreCase = true)) {
                    "Connected to $currentSSID"
                } else {
                    "Not connected to androidwifi"
                }
            } else {
                connectionStatus = "Not connected to androidwifi"
            }
            delay(5000L) // Check every 5 seconds
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the connection status
            Text(
                text = connectionStatus,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Button to navigate back home
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Go to Home")
            }
        }
    }
}

 */

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun ShellyScreen(
    navController: NavHostController,
    wifiScanner: WifiScanner,
    logs: MutableList<String>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        //Color(0xFF2196F3),
                        Color.White,
                        Color.White,
                        MaterialTheme.colorScheme.primary
                        ),
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text("Shelly Plug Setup") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            // Logs Display Section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Logs",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Column(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    logs.forEach { log ->
                        Text(
                            text = log,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { logs.clear() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = "Clear Logs")
                }
            }

            // Wi-Fi Scan Button
            Button(
                onClick = { wifiScanner.checkPermissionsAndScan() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Scan for Shelly Plug and Connect")
            }

            // Navigate to Home Button
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Go to Home")
            }
        }
    }
}


@Composable
fun SignupScreen(navController: NavHostController) {
    // Add ViewModel
    val viewModel: AuthViewModel = viewModel()
    var authState by remember { mutableStateOf<AuthViewModel.AuthState>(AuthViewModel.AuthState.Idle) }

    // State variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // State to track if verification info should be shown
    var showVerificationInfo by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top section with back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Signup form card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Email field
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = null // Clear error when user types
                        },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        },
                        isError = errorMessage?.contains("email", ignoreCase = true) == true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true
                    )

                    // Password field
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null // Clear error when user types
                        },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible)
                                        Icons.Outlined.Visibility
                                    else
                                        Icons.Outlined.VisibilityOff,
                                    contentDescription = if (isPasswordVisible)
                                        "Hide password"
                                    else
                                        "Show password"
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        isError = errorMessage?.contains("password", ignoreCase = true) == true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true
                    )

                    // Confirm Password field
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            errorMessage = null // Clear error when user types
                        },
                        label = { Text("Confirm Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Confirm Password"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (isConfirmPasswordVisible)
                                        Icons.Outlined.Visibility
                                    else
                                        Icons.Outlined.VisibilityOff,
                                    contentDescription = if (isConfirmPasswordVisible)
                                        "Hide password"
                                    else
                                        "Show password"
                                )
                            }
                        },
                        visualTransformation = if (isConfirmPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        isError = errorMessage?.contains("password", ignoreCase = true) == true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true
                    )

                    // Verification Info Card (shows only after successful signup)
                    AnimatedVisibility(
                        visible = showVerificationInfo,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Account Created Successfully!",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "A verification code has been sent to your email.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        navController.navigate("verify-email/${email}")
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Enter Verification Code")
                                }
                            }
                        }
                    }

                    // Error message display
                    if (errorMessage != null && !showVerificationInfo) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorMessage!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up button
            if (!showVerificationInfo) {
                Button(
                    onClick = {
                        when {
                            email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                                errorMessage = "Please fill in all fields"
                            }
                            password != confirmPassword -> {
                                errorMessage = "Passwords do not match"
                            }
                            else -> {
                                viewModel.signup(email, password) { state ->
                                    authState = state
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = authState !is AuthViewModel.AuthState.Loading
                ) {
                    when (authState) {
                        is AuthViewModel.AuthState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        else -> {
                            Text("Sign Up")
                        }
                    }
                }

                // Already have an account? Login link
                TextButton(
                    onClick = { navController.navigate("login") }
                ) {
                    Text("Already have an account? Login")
                }
            }
        }
    }

    // Handle authentication states
    LaunchedEffect(authState) {
        when (authState) {
            is AuthViewModel.AuthState.Success -> {
                // Show verification info and options
                errorMessage = null
                showVerificationInfo = true
            }
            is AuthViewModel.AuthState.Error -> {
                errorMessage = (authState as AuthViewModel.AuthState.Error).message
                showVerificationInfo = false
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    // Add ViewModel
    val viewModel: AuthViewModel = viewModel()
    var authState by remember { mutableStateOf<AuthViewModel.AuthState>(AuthViewModel.AuthState.Idle) }
    val context = LocalContext.current

    // State variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var rememberMe by remember { mutableStateOf(false) }

    // Initialize SessionManager
    val sessionManager = remember { SessionManager(context) }

    // Check if user is already logged in with "Remember Me" enabled
    LaunchedEffect(Unit) {
        if (sessionManager.isLoggedIn() && sessionManager.isRememberMeEnabled()) {
            // Load user's devices from persistent storage
            val deviceManager = PersistentDeviceManager(context)
            deviceManager.loadDevices()

            // Navigate to home screen
            navController.navigate("home") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top section with back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Login form card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Email field
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = null // Clear error when user types
                        },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        },
                        isError = errorMessage?.contains("email", ignoreCase = true) == true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true
                    )

                    // Password field
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null // Clear error when user types
                        },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible)
                                        Icons.Outlined.Visibility
                                    else
                                        Icons.Outlined.VisibilityOff,
                                    contentDescription = if (isPasswordVisible)
                                        "Hide password"
                                    else
                                        "Show password"
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        isError = errorMessage?.contains("password", ignoreCase = true) == true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true
                    )

                    // Remember Me checkbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { newValue -> rememberMe = newValue }
                            )
                            Text("Remember Me")
                        }

                        // Forgot password button
                        TextButton(
                            onClick = { navController.navigate("forgot-password") }
                        ) {
                            Text("Forgot Password?")
                        }
                    }

                    // Error message display
                    if (errorMessage != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorMessage!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Login button
            Button(
                onClick = {
                    when {
                        email.isEmpty() || password.isEmpty() -> {
                            errorMessage = "Please fill in all fields"
                        }
                        else -> {
                            viewModel.login(email, password) { state ->
                                authState = state
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = authState !is AuthViewModel.AuthState.Loading
            ) {
                when (authState) {
                    is AuthViewModel.AuthState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    else -> {
                        Text("Login")
                    }
                }
            }

            // Don't have an account? Sign up link
            TextButton(
                onClick = { navController.navigate("signup") }
            ) {
                Text("Don't have an account? Sign up")
            }
        }
    }

    // Handle authentication states
    LaunchedEffect(authState) {
        when (authState) {
            is AuthViewModel.AuthState.Success -> {
                val successState = authState as AuthViewModel.AuthState.Success
                val userId = successState.userId ?: "user_id"

                // Create a login session with "Remember Me" preference
                sessionManager.createLoginSession(userId, email, rememberMe)

                // Load this user's specific devices from persistent storage
                val deviceManager = PersistentDeviceManager(context)
                deviceManager.loadDevicesForUser(email)

                // Navigate to home screen on successful login
                navController.navigate("home") {
                    popUpTo(0) { inclusive = true }
                }
            }
            is AuthViewModel.AuthState.Error -> {
                errorMessage = (authState as AuthViewModel.AuthState.Error).message
            }
            is AuthViewModel.AuthState.PasswordResetRequested -> {
                errorMessage = (authState as AuthViewModel.AuthState.PasswordResetRequested).message
            }
            is AuthViewModel.AuthState.PasswordResetSuccess -> {
                errorMessage = (authState as AuthViewModel.AuthState.PasswordResetSuccess).message
                delay(2000) // Show success message briefly
                // Clear error message
                errorMessage = null
            }
            else -> {}
        }
    }
}


/* 04-17-2025 working homescreen, welcomesection, devicegrid, devicecatcard, bottombuttons, devicecat class
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    sessionManager: SessionManager // Add this parameter
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Home Machine") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    // Settings icon
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Welcome section
            WelcomeSection()

            // Quick Actions Grid
            DeviceGrid(navController)

            // Bottom buttons
            BottomButtons(navController)
        }
    }
}

@Composable
private fun WelcomeSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Home",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Control your home environment with ease",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DeviceGrid(navController: NavHostController) {
    val deviceCategories = listOf(
        DeviceCategory("Lights", Icons.Default.Lightbulb, "lights"),
        DeviceCategory("Plugs", Icons.Default.PowerSettingsNew, "plugs"),
        DeviceCategory("Cameras", Icons.Default.Videocam, "cameras"),
        DeviceCategory("Sensors", Icons.Default.SensorsOff, "sensors")
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Device Categories",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            deviceCategories.take(2).forEach { category ->
                DeviceCategoryCard(
                    category = category,
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(category.route) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            deviceCategories.takeLast(2).forEach { category ->
                DeviceCategoryCard(
                    category = category,
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(category.route) }
                )
            }
        }
    }
}

@Composable
private fun DeviceCategoryCard(
    category: DeviceCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .shadow(4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun BottomButtons(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            onClick = { navController.navigate("schedule") },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Schedule")
        }

        Button(
            onClick = { navController.navigate("addDevice") },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Device")
        }
    }
}

private data class DeviceCategory(
    val name: String,
    val icon: ImageVector,
    val route: String
)


 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    sessionManager: SessionManager,
    deviceController: DeviceController,
    onTurnLightOn: () -> Unit,
    onTurnLightOff: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My Home Machine",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary,
                            //Color(0xFF2196F3),
                            Color.White,
                            Color.White,
                            MaterialTheme.colorScheme.primary
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp) // so content doesn't hide behind the buttons
            ) {
                WelcomeSection()
                var isLightOn by remember { mutableStateOf(false) }
                DeviceGrid(
                    navController = navController,
                    deviceController = deviceController,
                    isLightOn = isLightOn,
                    onTurnLightOn = {
                        onTurnLightOn()
                        isLightOn = true
                    },
                    onTurnLightOff = {
                        onTurnLightOff()
                        isLightOn = false
                    }
                )
            }

            BottomButtons(navController) // now absolutely positioned
        }

    }
}

@Composable
private fun WelcomeSection() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Welcome Home",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Welcome Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Control your home environment with ease",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@Composable
private fun DeviceGrid(
    navController: NavHostController,
    deviceController: DeviceController,
    isLightOn: Boolean,
    onTurnLightOn: () -> Unit,
    onTurnLightOff: () -> Unit
) {
    val deviceCategories = listOf(
        DeviceCategory("Lights", Icons.Default.Lightbulb, "lights"),
        DeviceCategory("Plugs", Icons.Default.PowerSettingsNew, "plugs"),
        DeviceCategory("Cameras", Icons.Default.Videocam, "cameras"),
        DeviceCategory("Sensors", Icons.Default.SensorsOff, "sensors")
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "Device Categories",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        for (chunk in deviceCategories.chunked(2)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                chunk.forEach { category ->
                    DeviceCategoryCard(
                        category = category,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(category.route) },
                        onTurnOnPlug = { deviceController.turnOnPlug() },
                        onTurnOffPlug = { deviceController.turnOffPlug() },
                        isLightOn = if (category.name == "Lights") isLightOn else false,
                        onTogglePower = if (category.name == "Lights") {
                            {
                                if (isLightOn) onTurnLightOff() else onTurnLightOn()
                            }
                        } else null
                    )
                }
                if (chunk.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/* working toggle switch light
@Composable
private fun DeviceCategoryCard(
    category: DeviceCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLightOn: Boolean = false, // 🔁 current light state
    onTogglePower: (() -> Unit)? = null // 🔁 toggle logic
) {
    val supportsToggle = category.name in listOf("Lights", "Plugs")

    // Static mock device list with initial ON/OFF state
    val mockDevices = mapOf(
        "Lights" to listOf("LIFX A19 Bulb" to true),
        "Plugs" to listOf("Shelly Plug" to false),
        "Cameras" to listOf("Raspberry PI" to true),
        "Sensors" to listOf("SWITCHBOT sensor" to true)
    )

    val devices = mockDevices[category.name] ?: emptyList()

    // Remembered local state for toggles (only for interactive ones)
    val deviceStates = remember {
        devices.map { (name, isOn) -> name to mutableStateOf(isOn) }
    }

    ElevatedCard(
        modifier = modifier
            .heightIn(min = 180.dp)
            .fillMaxWidth()
            .shadow(4.dp),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${devices.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // List of devices with icons and (optional) toggle buttons
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                deviceStates.forEach { (deviceName, isOnState) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isOnState.value) Icons.Default.Power else Icons.Default.PowerSettingsNew,
                                contentDescription = if (isOnState.value) "On" else "Off",
                                modifier = Modifier.size(16.dp),
                                tint = if (isOnState.value) Color(0xFF4CAF50) else Color(0xFFB0BEC5)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = deviceName,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        if (supportsToggle) {
                            IconButton(onClick = {
                                if (category.name == "Lights") {
                                    onTogglePower?.invoke()
                                }
                            }) {
                                Icon(
                                    imageVector = if (isLightOn) Icons.Default.ToggleOn else Icons.Default.ToggleOff,
                                    contentDescription = "Toggle",
                                    tint = if (isLightOn) Color(0xFF4CAF50) else Color(0xFFB0BEC5),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

 */

@Composable
private fun DeviceCategoryCard(
    category: DeviceCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLightOn: Boolean = false,
    onTogglePower: (() -> Unit)? = null,
    onTurnOnPlug: (() -> Unit)? = null,
    onTurnOffPlug: (() -> Unit)? = null
) {
    val supportsToggle = category.name in listOf("Lights", "Plugs")

    // Get real devices for each category
    val lights = DeviceManager.knownLights
    val plugs = DeviceManager.knownPlugs
    val cameras = DeviceManager.knownCameras
    val sensors = DeviceManager.knownSensors

    val realDevices = when (category.name) {
        "Lights" -> lights
        "Plugs" -> plugs
        "Cameras" -> cameras
        "Sensors" -> sensors
        else -> emptyList()
    }

    // If no devices have been added, don't show the list
    val showDeviceList = realDevices.isNotEmpty()

    // Maintain UI toggle state per device name
    val deviceStates = remember {
        realDevices.associateWith { mutableStateOf(isLightOn) }
    }

    ElevatedCard(
        modifier = modifier
            .heightIn(min = if (showDeviceList) 180.dp else 140.dp)
            .fillMaxWidth()
            .shadow(4.dp),
        shape = RoundedCornerShape(5.dp),
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${realDevices.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            // Device list
            if (showDeviceList) {
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    realDevices.forEach { deviceName ->
                        val isOnState = deviceStates[deviceName] ?: remember { mutableStateOf(false) }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isOnState.value) Icons.Default.Power else Icons.Default.PowerSettingsNew,
                                    contentDescription = if (isOnState.value) "On" else "Off",
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isOnState.value) Color(0xFF4CAF50) else Color(0xFFB0BEC5)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = deviceName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }

                            if (supportsToggle) {
                                IconButton(onClick = {
                                    isOnState.value = !isOnState.value
                                    when (category.name) {
                                        "Lights" -> onTogglePower?.invoke()
                                        "Plugs" -> {
                                            if (isOnState.value) {
                                                onTurnOnPlug?.invoke()
                                            } else {
                                                onTurnOffPlug?.invoke()
                                            }
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (isOnState.value) Icons.Default.ToggleOn else Icons.Default.ToggleOff,
                                        contentDescription = "Toggle",
                                        tint = if (isOnState.value) Color(0xFF4CAF50) else Color(0xFFB0BEC5),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}





@Composable
private fun BottomButtons(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { navController.navigate("schedule") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }

            Button(
                onClick = { navController.navigate("addDevice") },
                modifier = Modifier.weight(3f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Device", style = MaterialTheme.typography.labelLarge)
            }

            // Smart suggestions button with badge
            Box(modifier = Modifier.weight(1f)) {
                // Get context first, then use it
                val context = LocalContext.current
                val usageTrackingManager = remember { UsageTrackingManager(context) }
                var suggestionCount by remember { mutableStateOf(0) }

                // Update count when screen appears
                LaunchedEffect(Unit) {
                    suggestionCount = usageTrackingManager.getSavedSuggestions().size
                }

                OutlinedButton(
                    onClick = { navController.navigate("suggestions") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Show badge if suggestions available
                if (suggestionCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = (-8).dp, end = (-8).dp)
                    ) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error
                        ) {
                            Text(
                                text = suggestionCount.toString(),
                                color = MaterialTheme.colorScheme.onError
                            )
                        }
                    }
                }
            }
        }
    }
}





private data class DeviceCategory(
    val name: String,
    val icon: ImageVector,
    val route: String
)





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceScreen(onDeviceAdded: () -> Unit, navController: NavHostController) {
    var selectedDeviceType by remember { mutableStateOf("") }
    var selectedDeviceName by remember { mutableStateOf<String?>(null) }
    var customDeviceName by remember { mutableStateOf("") }
    var typeExpanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val deviceManager = remember { PersistentDeviceManager(context) }

    val deviceTypes = listOf("Camera", "Light", "Plug", "Sensor")
    val devicesByType = mapOf(
        "Camera" to listOf("RaspberryPiCamera"),
        "Light" to listOf("LIFX Smart Light"),
        "Plug" to listOf("Shelly Smart Plug"),
        "Sensor" to listOf("SwitchBot Meter")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1B5E20), // Top green
                        Color.White,
                        Color.White,
                        MaterialTheme.colorScheme.secondary  // Bottom blue
                    ),
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopAppBar(
                title = { Text("Add New Device") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("1. Select Device Type", style = MaterialTheme.typography.titleMedium)

                    ExposedDropdownMenuBox(
                        expanded = typeExpanded,
                        onExpandedChange = { typeExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedDeviceType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Device Type") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(typeExpanded)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                                cursorColor = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false }
                        ) {
                            deviceTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        selectedDeviceType = type
                                        selectedDeviceName = null
                                        typeExpanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = when (type) {
                                                "Camera" -> Icons.Default.Videocam
                                                "Light" -> Icons.Default.LightMode
                                                "Plug" -> Icons.Default.Power
                                                else -> Icons.Default.Sensors
                                            },
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (selectedDeviceType.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("2. Select Available Device", style = MaterialTheme.typography.titleMedium)

                        devicesByType[selectedDeviceType]?.forEach { device ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedDeviceName = device }
                                    .padding(vertical = 8.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedDeviceName == device,
                                    onClick = { selectedDeviceName = device },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colorScheme.secondary
                                    )
                                )
                                Text(device)
                            }
                        }
                    }
                }
            }

            if (selectedDeviceName != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("3. Set Custom Name (Optional)", style = MaterialTheme.typography.titleMedium)

                        OutlinedTextField(
                            value = customDeviceName,
                            onValueChange = { customDeviceName = it },
                            label = { Text("Custom Name") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                                cursorColor = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(selectedDeviceName ?: "") }
                        )

                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("shelly") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .animateContentSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    ),
                    enabled = selectedDeviceType == "Plug" && selectedDeviceName == "Shelly Smart Plug"
                ) {
                    Text("Navigate to add Shelly Smart Plug")
                }
            }

            if (selectedDeviceName != null) {
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Device")
                }

            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Add Device") },
            text = {
                Text(
                    "Add ${customDeviceName.ifEmpty { selectedDeviceName }} " +
                            "to your $selectedDeviceType collection?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val deviceName = customDeviceName.ifEmpty { selectedDeviceName ?: "" }
                        deviceManager.addDevice(deviceName, selectedDeviceType)
                        showConfirmDialog = false
                        onDeviceAdded()
                        navController.navigate("home")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text("Cancel")
                }
            }

        )
    }
}




//working lightscreen 2.0
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightsScreen(navController: NavHostController) {
    val knownLights = DeviceManager.knownLights
    var selectedLight by remember { mutableStateOf<String?>(null) }
    var isLightOn by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.White) }
    var brightness by remember { mutableStateOf(0.8f) }
    var showScheduleDialog by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lights Control") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Light Selection Card
            LightSelectionCard(
                knownLights = knownLights,
                selectedLight = selectedLight,
                onLightSelected = { selectedLight = it }
            )

            if (selectedLight != null) {
                Spacer(modifier = Modifier.height(16.dp))

                        ExposedDropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false }
                        ) {
                            deviceTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        selectedDeviceType = type
                                        selectedDeviceName = null
                                        typeExpanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = when (type) {
                                                "Camera" -> Icons.Default.Videocam
                                                "Light" -> Icons.Default.LightMode
                                                "Plug" -> Icons.Default.Power
                                                else -> Icons.Default.Sensors
                                            },
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

                Spacer(modifier = Modifier.height(16.dp))

                // Color Selection Card
                ColorSelectionCard(
                    selectedColor = selectedColor,
                    onColorSelected = { selectedColor = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Schedule and Save Buttons
                ActionButtons(
                    onScheduleClick = { showScheduleDialog = true },
                    onSaveClick = { showConfirmation = true }
                )
            }
        }
    }

    // Schedule Dialog
    if (showScheduleDialog) {
        EnhancedScheduleDialog(onDismiss = { showScheduleDialog = false })
    }

    // Confirmation Dialog
    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Success") },
            text = { Text("Light settings have been saved successfully.") },
            confirmButton = {
                Button(onClick = {
                    showConfirmation = false
                    navController.navigateUp()
                }) {
                    Text("OK")
                }
            }
        )
    }
}

*/

//working lightscreen2.1
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightsScreen(
    navController: NavHostController,
    onTurnLightOn: () -> Unit,   // Accept functions
    onTurnLightOff: () -> Unit   // Accept functions
) {
    val knownLights = DeviceManager.knownLights
    var selectedLight by remember { mutableStateOf<String?>(null) }
    var isLightOn by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.White) }
    var brightness by remember { mutableStateOf(0.8f) }
    var showScheduleDialog by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lights Control") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            LightSelectionCard(
                knownLights = knownLights,
                selectedLight = selectedLight,
                onLightSelected = { selectedLight = it }
            )

            if (selectedLight != null) {
                Spacer(modifier = Modifier.height(16.dp))
                LightControlsCard(
                    isLightOn = isLightOn,
                    onPowerChange = {
                        if (isLightOn) {
                            onTurnLightOff()
                        } else {
                            onTurnLightOn()
                        }
                        isLightOn = !isLightOn
                    },
                    brightness = brightness,
                    onBrightnessChange = { brightness = it },
                    selectedColor = selectedColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                ColorSelectionCard(
                    selectedColor = selectedColor,
                    onColorSelected = { selectedColor = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ActionButtons(
                    onScheduleClick = { showScheduleDialog = true },
                    onSaveClick = { showConfirmation = true }
                )
            }
        }
    }

    if (showScheduleDialog) {
        EnhancedScheduleDialog(onDismiss = { showScheduleDialog = false })
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Success") },
            text = { Text("Light settings have been saved successfully.") },
            confirmButton = {
                Button(onClick = {
                    showConfirmation = false
                    navController.navigateUp()
                }) {
                    Text("OK")
                }
            }
        )
    }
}

 */



/* 2.43 working lightscreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightsScreen(
    navController: NavHostController,
    onTurnLightOn: () -> Unit,
    onTurnLightOff: () -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onSetColor: (Color) -> Unit
) {
    val knownLights = DeviceManager.knownLights
    var selectedLight by remember { mutableStateOf<String?>(null) }
    var isLightOn by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.White) }
    var brightness by remember { androidx.compose.runtime.mutableFloatStateOf(0.8f) }
    var showScheduleDialog by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf<String?>(null) }

    // Get context and PersistentDeviceManager
    val context = LocalContext.current
    val deviceManager = remember { PersistentDeviceManager(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lights Control") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Device Selection Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Select Light",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (knownLights.isEmpty()) {
                        Text(
                            text = "No lights added yet. Add a light from the home screen.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        knownLights.forEach { light ->
                            DeviceListItem(
                                deviceName = light,
                                onSelect = { selectedLight = light },
                                onDelete = { showDeleteConfirmation = light },
                                isSelected = light == selectedLight
                            )
                        }
                    }
                }
            }

            if (selectedLight != null) {
                Spacer(modifier = Modifier.height(16.dp))
                LightControlsCard(
                    isLightOn = isLightOn,
                    onPowerChange = {
                        if (isLightOn) {
                            onTurnLightOff()
                            isLightOn = false
                        } else {
                            onTurnLightOn()
                            isLightOn = true
                        }
                    },
                    brightness = brightness,
                    onBrightnessChange = {
                        brightness = it
                        onBrightnessChange(it)
                    },
                    selectedColor = selectedColor
                )
                Spacer(modifier = Modifier.height(16.dp))

                ColorSelectionCard(
                    selectedColor = selectedColor,
                    onColorSelected = { color ->
                        selectedColor = color
                        onSetColor(color)
                    },
                    setColor = onSetColor // Pass setColor function properly
                )
                Spacer(modifier = Modifier.height(16.dp))
                ActionButtons(
                    onScheduleClick = { showScheduleDialog = true },
                    onSaveClick = { showConfirmation = true }
                )
            }
        }
    }

    // Delete confirmation dialog
    showDeleteConfirmation?.let { deviceName ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Device") },
            text = { Text("Are you sure you want to remove $deviceName?") },
            confirmButton = {
                Button(
                    onClick = {
                        deviceManager.removeDevice(deviceName, "Light")
                        if (selectedLight == deviceName) {
                            selectedLight = null
                        }
                        showDeleteConfirmation = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = null }) {
                    Text("Cancel")
                }
            }
        )
    }


    // Show schedule dialog
    if (showScheduleDialog && selectedLight != null) {
        EnhancedScheduleDialog(
            onDismiss = { showScheduleDialog = false },
            deviceName = selectedLight!!,  // Pass the selected light name
            deviceType = "Light",          // Set device type as "Light"
            onSaveSchedule = { scheduleString ->
                try {
                    deviceManager.saveSchedule(scheduleString)
                    // Show confirmation
                    Toast.makeText(context, "Schedule saved", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("LightsScreen", "Error saving schedule: ${e.message}", e)
                    // Show error
                    Toast.makeText(context, "Failed to save schedule", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Success") },
            text = { Text("Light settings have been saved successfully.") },
            confirmButton = {
                Button(onClick = {
                    showConfirmation = false
                    navController.navigateUp()
                }) {
                    Text("OK")
                }
            }
        )
    }
}
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightsScreen(
    navController: NavHostController,
    onTurnLightOn: () -> Unit,
    onTurnLightOff: () -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onSetColor: (Color) -> Unit
) {
    val knownLights = DeviceManager.knownLights
    var selectedLight by remember { mutableStateOf<String?>(null) }
    var isLightOn by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.White) }
    var brightness by remember { androidx.compose.runtime.mutableFloatStateOf(0.8f) }
    var showScheduleDialog by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val deviceManager = remember { PersistentDeviceManager(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lights Control") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary,
                                    //Color(0xFF2196F3),
                            Color.White,
                            Color.White,
                            MaterialTheme.colorScheme.primary
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Select Light",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (knownLights.isEmpty()) {
                            Text(
                                text = "No lights added yet. Add a light from the home screen.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            knownLights.forEach { light ->
                                DeviceListItem(
                                    deviceName = light,
                                    onSelect = { selectedLight = light },
                                    onDelete = { showDeleteConfirmation = light },
                                    isSelected = light == selectedLight
                                )
                            }
                        }
                    }
                }

                if (selectedLight != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    LightControlsCard(
                        isLightOn = isLightOn,
                        onPowerChange = {
                            if (isLightOn) {
                                onTurnLightOff()
                                isLightOn = false
                            } else {
                                onTurnLightOn()
                                isLightOn = true
                            }
                        },
                        brightness = brightness,
                        onBrightnessChange = {
                            brightness = it
                            onBrightnessChange(it)
                        },
                        selectedColor = selectedColor
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ColorSelectionCard(
                        selectedColor = selectedColor,
                        onColorSelected = { color ->
                            selectedColor = color
                            onSetColor(color)
                        },
                        setColor = onSetColor
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ActionButtons(
                        onScheduleClick = { showScheduleDialog = true },
                        onSaveClick = { showConfirmation = true }
                    )
                }
            }
        }
    }

    showDeleteConfirmation?.let { deviceName ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Device") },
            text = { Text("Are you sure you want to remove $deviceName?") },
            confirmButton = {
                Button(
                    onClick = {
                        deviceManager.removeDevice(deviceName, "Light")
                        if (selectedLight == deviceName) {
                            selectedLight = null
                        }
                        showDeleteConfirmation = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showScheduleDialog && selectedLight != null) {
        EnhancedScheduleDialog(
            onDismiss = { showScheduleDialog = false },
            deviceName = selectedLight!!,
            deviceType = "Light",
            onSaveSchedule = { scheduleString ->
                try {
                    deviceManager.saveSchedule(scheduleString)
                    Toast.makeText(context, "Schedule saved", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("LightsScreen", "Error saving schedule: ${e.message}", e)
                    Toast.makeText(context, "Failed to save schedule", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Success") },
            text = { Text("Light settings have been saved successfully.") },
            confirmButton = {
                Button(onClick = {
                    showConfirmation = false
                    navController.navigateUp()
                }) {
                    Text("OK")
                }
            }
        )
    }
}




// USED FOR SELECTING WHICH LIGHT YOU WANT TO USE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LightSelectionCard(
    knownLights: List<String>,
    selectedLight: String?,
    onLightSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Select Light",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedLight ?: "Choose a light",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    knownLights.forEach { light ->
                        DropdownMenuItem(
                            text = { Text(light) },
                            onClick = {
                                onLightSelected(light)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun LightControlsCard(
    isLightOn: Boolean,
    onPowerChange: (Boolean) -> Unit,
    brightness: Float,
    onBrightnessChange: (Float) -> Unit,
    selectedColor: Color
) {
    var rotationState by remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    val rotation by animateFloatAsState(
        targetValue = rotationState,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    LaunchedEffect(brightness) {
        rotationState = brightness * 360f
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Power Button with animation
            IconButton(
                onClick = { onPowerChange(!isLightOn) },
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = if (isLightOn) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Power,
                    contentDescription = if (isLightOn) "Turn Off" else "Turn On",
                    tint = if (isLightOn) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(32.dp)
                        .animateContentSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Brightness Control with Sun Icon
            Text(
                text = "Brightness ${(brightness * 100).roundToInt()}%",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.WbSunny,
                    contentDescription = "Minimum brightness",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Slider(
                    value = brightness,
                    onValueChange = onBrightnessChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )

                Icon(
                    imageVector = Icons.Outlined.WbSunny,
                    contentDescription = "Maximum brightness",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ColorSelectionCard(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    setColor: (Color) -> Unit // Add this parameter
) {
    var showCustomColorPicker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Color",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ColorButton(Color.Red, selectedColor, onColorSelected, setColor)
                    ColorButton(Color.Green, selectedColor, onColorSelected, setColor)
                    ColorButton(Color.Blue, selectedColor, onColorSelected, setColor)
                    ColorButton(Color.Yellow, selectedColor, onColorSelected, setColor)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ColorButton(Color.Magenta, selectedColor, onColorSelected, setColor)
                    ColorButton(Color.Cyan, selectedColor, onColorSelected, setColor)
                    ColorButton(Color.White, selectedColor, onColorSelected, setColor)
                    ColorButton(Color(0xFFFF8C00), selectedColor, onColorSelected, setColor) // Orange
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ColorButton(Color(0xFF800080), selectedColor, onColorSelected, setColor) // Purple
                    ColorButton(Color(0xFF98FB98), selectedColor, onColorSelected, setColor) // Pale green
                    ColorButton(Color(0xFF87CEEB), selectedColor, onColorSelected, setColor) // Sky blue
                    ColorButton(Color(0xFFFFB6C1), selectedColor, onColorSelected, setColor) // Light pink
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Custom color button
            OutlinedButton(
                onClick = { showCustomColorPicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "Custom color"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Custom Color")
            }
        }
    }

    if (showCustomColorPicker) {
        CustomColorPickerDialog(
            initialColor = selectedColor,
            onColorSelected = {
                onColorSelected(it)
                showCustomColorPicker = false
            },
            onDismiss = { showCustomColorPicker = false }
        )
    }
}

@Composable
private fun CustomColorPickerDialog(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    var red by remember { androidx.compose.runtime.mutableFloatStateOf(initialColor.red) }
    var green by remember { androidx.compose.runtime.mutableFloatStateOf(initialColor.green) }
    var blue by remember { androidx.compose.runtime.mutableFloatStateOf(initialColor.blue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Custom Color") },
        text = {
            Column {
                // Preview of selected color
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color(red, green, blue))
                        .clip(MaterialTheme.shapes.medium)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // RGB Sliders
                Text("Red")
                Slider(
                    value = red,
                    onValueChange = { red = it },
                    colors = SliderDefaults.colors(thumbColor = Color.Red)
                )

                Text("Green")
                Slider(
                    value = green,
                    onValueChange = { green = it },
                    colors = SliderDefaults.colors(thumbColor = Color.Green)
                )

                Text("Blue")
                Slider(
                    value = blue,
                    onValueChange = { blue = it },
                    colors = SliderDefaults.colors(thumbColor = Color.Blue)
                )

                // Hex color code display
                Text(
                    text = String.format("#%06X", (Color(red, green, blue).toArgb() and 0xFFFFFF)),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onColorSelected(Color(red, green, blue)) }
            ) {
                Text("Select")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun toggleMotionDetection(device: String, onComplete: () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://10.154.13.1:5000/toggle_motion/$device")
                .put(okhttp3.RequestBody.create(null, ByteArray(0))) // Empty PUT body
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                onComplete()  // Update UI state
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Composable
fun ColorButton(
    color: Color,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    setColor: (Color) -> Unit
) {
    Button(
        onClick = {
            onColorSelected(color)
            setColor(color) // Change the light color
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        modifier = Modifier
            .size(48.dp)
            .border(
                width = if (color == selectedColor) 2.dp else 0.dp,
                color = Color.Black,
                shape = CircleShape
            )
    ) {}
}

@Composable
private fun ActionButtons(
    onScheduleClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            onClick = onScheduleClick,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Schedule")
        }

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Save")
        }
    }
}

// Plugs section
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlugsScreen(navController: NavHostController, deviceController: DeviceController) {
    var knownPlugs = DeviceManager.knownPlugs
    var selectedPlug by remember { mutableStateOf<String?>(null) }
    var isPlugOn by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val deviceManager = remember { PersistentDeviceManager(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Plugs") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary,
                            //Color(0xFF2196F3),
                            Color.White,
                            Color.White,
                            MaterialTheme.colorScheme.primary
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Plug Selection Card
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Select Smart Plug",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (knownPlugs.isEmpty()) {
                            Text(
                                text = "No plugs added yet. Add a plug from the home screen.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            knownPlugs.forEach { plug ->
                                DeviceListItem(
                                    deviceName = plug,
                                    onSelect = { selectedPlug = plug },
                                    onDelete = { showDeleteConfirmation = plug },
                                    isSelected = plug == selectedPlug
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Power Control Card
                if (selectedPlug != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = {
                                    if (isPlugOn) {
                                        deviceController.turnOffPlug()
                                    } else {
                                        deviceController.turnOnPlug()
                                    }
                                    isPlugOn = !isPlugOn
                                },
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(
                                        if (isPlugOn) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant,
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    Icons.Default.Power,
                                    contentDescription = if (isPlugOn) "Turn Off" else "Turn On",
                                    tint = if (isPlugOn) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Text(
                                text = if (isPlugOn) "ON" else "OFF",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    showDeleteConfirmation?.let { deviceName ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Device") },
            text = { Text("Are you sure you want to remove $deviceName?") },
            confirmButton = {
                Button(
                    onClick = {
                        deviceManager.removeDevice(deviceName, "Plug")
                        if (selectedPlug == deviceName) {
                            selectedPlug = null
                        }
                        showDeleteConfirmation = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamerasScreen(navController: NavHostController) {
    var knownCameras = DeviceManager.knownCameras
    var selectedCamera by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf<String?>(null) }
    var motionDetectionLight by remember { mutableStateOf(true) }
    var motionDetectionPlug by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val deviceManager = remember { PersistentDeviceManager(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cameras") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary,
                            //Color(0xFF2196F3),
                            Color.White,
                            Color.White,
                            MaterialTheme.colorScheme.primary
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Camera Selection Card
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Select Camera",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (knownCameras.isEmpty()) {
                            Text(
                                text = "No cameras added yet. Add a camera from the home screen.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            knownCameras.forEach { camera ->
                                DeviceListItem(
                                    deviceName = camera,
                                    onSelect = { selectedCamera = camera },
                                    onDelete = { showDeleteConfirmation = camera },
                                    isSelected = camera == selectedCamera
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Camera Preview Card
                if (selectedCamera != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AndroidView(
                                factory = { context ->
                                    WebView(context).apply {
                                        settings.javaScriptEnabled = true
                                        webViewClient = WebViewClient()
                                        loadUrl("http://10.154.13.1:5000/stream")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    toggleMotionDetection("light") {
                                        motionDetectionLight = !motionDetectionLight
                                        DeviceNotificationManager(context).sendDeviceNotification(
                                            deviceType = DeviceType.CAMERA,
                                            eventType = EventType.MOTION_DETECTED,
                                            deviceName = selectedCamera ?: "Camera",
                                            additionalDetails = if (motionDetectionLight)
                                                "Motion detection for Light enabled"
                                            else
                                                "Motion detection for Light disabled"
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (motionDetectionLight)
                                    "Disable Light Motion Detection"
                                else
                                    "Enable Light Motion Detection")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    toggleMotionDetection("plug") {
                                        motionDetectionPlug = !motionDetectionPlug
                                        DeviceNotificationManager(context).sendDeviceNotification(
                                            deviceType = DeviceType.CAMERA,
                                            eventType = EventType.MOTION_DETECTED,
                                            deviceName = selectedCamera ?: "Camera",
                                            additionalDetails = if (motionDetectionPlug)
                                                "Motion detection for Plug enabled"
                                            else
                                                "Motion detection for Plug disabled"
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (motionDetectionPlug)
                                    "Disable Plug Motion Detection"
                                else
                                    "Enable Plug Motion Detection")
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    showDeleteConfirmation?.let { deviceName ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Device") },
            text = { Text("Are you sure you want to remove $deviceName?") },
            confirmButton = {
                Button(
                    onClick = {
                        deviceManager.removeDevice(deviceName, "Camera")
                        if (selectedCamera == deviceName) {
                            selectedCamera = null
                        }
                        showDeleteConfirmation = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorsScreen(navController: NavHostController) {
    val knownSensors = DeviceManager.knownSensors
    var selectedSensor by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf<String?>(null) }
    var currentMeterStatus by remember { mutableStateOf<MeterStatus?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showCelsius by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val deviceManager = remember { PersistentDeviceManager(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sensors") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary,
                            //Color(0xFF2196F3),
                            Color.White,
                            Color.White,
                            MaterialTheme.colorScheme.primary
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Sensor Selection Card
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Select Sensor",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        if (knownSensors.isEmpty()) {
                            Text(
                                text = "No sensors added yet. Add a sensor from the home screen.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            knownSensors.forEach { sensor ->
                                DeviceListItem(
                                    deviceName = sensor,
                                    onSelect = {
                                        selectedSensor = sensor
                                        DeviceNotificationManager(context).sendDeviceNotification(
                                            deviceType = DeviceType.SENSOR,
                                            eventType = EventType.STATUS_CHANGE,
                                            deviceName = sensor,
                                            additionalDetails = "Sensor selected"
                                        )
                                    },
                                    onDelete = { showDeleteConfirmation = sensor },
                                    isSelected = sensor == selectedSensor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedSensor == "SwitchBot Meter") {
                    Button(
                        onClick = {
                            isLoading = true
                            CoroutineScope(Dispatchers.IO).launch {
                                forceCloudSync()
                                delay(1000)

                                val meterStatus = fetchMeterStatus()
                                withContext(Dispatchers.Main) {
                                    currentMeterStatus = meterStatus
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Get Meter Status")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            showCelsius = !showCelsius
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (showCelsius) "Switch to Fahrenheit" else "Switch to Celsius"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    MeterStatusDisplay(
                        currentMeterStatus = currentMeterStatus,
                        isLoading = isLoading,
                        showCelsius = showCelsius
                    )
                }
            }
        }
    }

    // Delete confirmation dialog
    showDeleteConfirmation?.let { deviceName ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Device") },
            text = { Text("Are you sure you want to remove $deviceName?") },
            confirmButton = {
                Button(
                    onClick = {
                        deviceManager.removeDevice(deviceName, "Sensor")
                        if (selectedSensor == deviceName) {
                            selectedSensor = null
                        }
                        showDeleteConfirmation = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}


/*
// WORKING
@Composable
fun MeterStatusDisplay(currentMeterStatus: MeterStatus?, isLoading: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "SwitchBot Meter",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (isLoading) {
                Text(
                    text = "Fetching meter status...",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                if (currentMeterStatus != null) {
                    Text(
                        text = "Temperature: ${"%.2f".format(currentMeterStatus.temperature)} °C",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Humidity: ${"%.2f".format(currentMeterStatus.humidity)} %",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Battery: ${currentMeterStatus.battery} %",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Dew Point: ${"%.2f".format(currentMeterStatus.dewPoint)} °C",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Heat Index: ${"%.2f".format(currentMeterStatus.heatIndex)} °C",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Absolute Humidity: ${"%.2f".format(currentMeterStatus.absoluteHumidity)} g/m³",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Saturation Vapor Pressure: ${"%.2f".format(currentMeterStatus.saturationVaporPressure)} hPa",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Vapor Pressure: ${"%.2f".format(currentMeterStatus.vaporPressure)} hPa",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Vapor Pressure Deficit: ${"%.2f".format(currentMeterStatus.vaporPressureDeficit)} hPa",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Mixing Ratio: ${"%.2f".format(currentMeterStatus.mixingRatio)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Enthalpy: ${"%.2f".format(currentMeterStatus.enthalpy)} kJ/kg",
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = "Press 'Get Meter Status' to fetch data",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

 */


fun cToF(celsius: Double): Double {
    return celsius * 9 / 5 + 32
}

// Custom radial gauge for Temperature
@Composable
fun TemperatureGauge(
    temperature: Double,
    minTemp: Double = 0.0,
    maxTemp: Double = 40.0,
    degreeLabel: String = "°C",
    modifier: Modifier = Modifier
) {
    val sweepAngle = (((temperature - minTemp) / (maxTemp - minTemp)) * 270.0).toFloat()
    Canvas(modifier = modifier) {
        // Background arc (full gauge)
        drawArc(
            color = Color.LightGray,
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = 50f)
        )
        // Foreground arc showing current temperature
        drawArc(
            color = Color.Red,
            startAngle = 135f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = 50f)
        )
        // Draw the temperature value in the center
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "${"%.3f".format(temperature)} $degreeLabel",
                size.width / 2,
                size.height / 2 + 12, // adjustment for vertical centering
                android.graphics.Paint().apply {
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 40f
                    color = android.graphics.Color.BLACK
                }
            )
        }
    }
}

// Custom radial gauge for Humidity
@Composable
fun HumidityGauge(
    humidity: Double,
    modifier: Modifier = Modifier
) {
    val sweepAngle = ((humidity) / 100.0) * 270f
    Canvas(modifier = modifier) {
        drawArc(
            color = Color.LightGray,
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = 50f)
        )
        drawArc(
            color = Color.Blue,
            startAngle = 135f,
            sweepAngle = sweepAngle.toFloat(),
            useCenter = false,
            style = Stroke(width = 50f)
        )
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "${"%.1f".format(humidity)} %",
                size.width / 2,
                size.height / 2 + 12,
                android.graphics.Paint().apply {
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 40f
                    color = android.graphics.Color.BLACK
                }
            )
        }
    }
}

// Custom radial gauge for Battery
@Composable
fun BatteryGauge(
    battery: Int,
    modifier: Modifier = Modifier
) {
    val sweepAngle = (battery / 100f) * 270f
    Canvas(modifier = modifier) {
        drawArc(
            color = Color.LightGray,
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = 50f)
        )
        drawArc(
            color = Color.Green,
            startAngle = 135f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = 50f)
        )
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "$battery%",
                size.width / 2,
                size.height / 2 + 12,
                android.graphics.Paint().apply {
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 40f
                    color = android.graphics.Color.BLACK
                }
            )
        }
    }
}

// Wrapper composable for Temperature
@Composable
fun TemperatureGaugeWithLabel(
    temperature: Double,
    minTemp: Double,
    maxTemp: Double,
    degreeLabel: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TemperatureGauge(
            temperature = temperature,
            minTemp = minTemp,
            maxTemp = maxTemp,
            degreeLabel = degreeLabel,
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Temperature",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// Wrapper composable for Humidity
@Composable
fun HumidityGaugeWithLabel(
    humidity: Double,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HumidityGauge(
            humidity = humidity,
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Humidity",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// Wrapper composable for Battery
@Composable
fun BatteryGaugeWithLabel(
    battery: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BatteryGauge(
            battery = battery,
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Battery",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun MeterStatusDisplay(
    currentMeterStatus: MeterStatus?,
    isLoading: Boolean,
    showCelsius: Boolean // <-- new parameter
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "SwitchBot Meter",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (isLoading) {
                Text(
                    text = "Fetching meter status...",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                if (currentMeterStatus != null) {
                    // Convert the temperature-based values if showCelsius is false
                    val temperature = if (showCelsius) {
                        currentMeterStatus.temperature
                    } else {
                        cToF(currentMeterStatus.temperature)
                    }
                    val dewPoint = if (showCelsius) {
                        currentMeterStatus.dewPoint
                    } else {
                        cToF(currentMeterStatus.dewPoint)
                    }
                    val heatIndex = if (showCelsius) {
                        currentMeterStatus.heatIndex
                    } else {
                        cToF(currentMeterStatus.heatIndex)
                    }

                    // Decide the label for degrees
                    val degreeLabel = if (showCelsius) "°C" else "°F"

                    // Stack each gauge vertically, each with its own label
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // If you want the gauge range to adjust for °F, you can do so:
                        TemperatureGaugeWithLabel(
                            temperature = temperature,
                            minTemp = if (showCelsius) 0.0 else 32.0,
                            maxTemp = if (showCelsius) 40.0 else 104.0,
                            degreeLabel = degreeLabel
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        HumidityGaugeWithLabel(humidity = currentMeterStatus.humidity)
                        Spacer(modifier = Modifier.height(24.dp))
                        BatteryGaugeWithLabel(battery = currentMeterStatus.battery)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // Display additional computed metrics as text
                    Text(
                        text = "Temperature: %.2f $degreeLabel".format(temperature),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Humidity: %.2f %%".format(currentMeterStatus.humidity),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Battery: ${currentMeterStatus.battery} %",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Dew Point: %.2f $degreeLabel".format(dewPoint),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Heat Index: %.2f $degreeLabel".format(heatIndex),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Absolute Humidity: %.2f g/m³".format(currentMeterStatus.absoluteHumidity),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Saturation Vapor Pressure: %.2f hPa".format(currentMeterStatus.saturationVaporPressure),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Vapor Pressure: %.2f hPa".format(currentMeterStatus.vaporPressure),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Vapor Pressure Deficit: %.2f hPa".format(currentMeterStatus.vaporPressureDeficit),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Mixing Ratio: %.2f".format(currentMeterStatus.mixingRatio),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Enthalpy: %.2f kJ/kg".format(currentMeterStatus.enthalpy),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = "Press 'Get Meter Status' to fetch data",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}




@Composable
fun AirQualityDisplay(airQualityHistory: List<Int>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Air Quality History",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            airQualityHistory.forEachIndexed { index, quality ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Time ${index + 1}")
                    Text("$quality AQI", style = MaterialTheme.typography.titleMedium)
                }
                if (index < airQualityHistory.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulePage(navController: NavHostController) {
    val context = LocalContext.current
    val deviceManager = remember { PersistentDeviceManager(context) }
    var showDeleteConfirmation by remember { mutableStateOf<String?>(null) }
    var showScheduleDialog by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            deviceManager.loadSchedules()
            isLoading = false
        } catch (e: Exception) {
            Log.e("SchedulePage", "Error loading schedules: ${e.message}", e)
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1B5E20), // Green top
                        Color.White,
                        Color.White, // Middle
                        MaterialTheme.colorScheme.secondary  // Blue bottom
                    ),
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = { Text("Device Schedules") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(  // Blue bottom
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )

                //colors = TopAppBarDefaults.topAppBarColors(
                //    containerColor = MaterialTheme.colorScheme.primary,
                //    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                //    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                //)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Current Schedules",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (DeviceManager.schedules.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No schedules found",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Tap the + button below to create one",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(DeviceManager.schedules) { schedule ->
                                    ScheduleItem(
                                        schedule = schedule,
                                        onDelete = { showDeleteConfirmation = schedule }
                                    )
                                }
                            }
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { showScheduleDialog = true },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Schedule",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }

    showDeleteConfirmation?.let { schedule ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Delete Schedule") },
            text = {
                Column {
                    Text("Are you sure you want to delete this schedule?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = schedule,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        try {
                            deviceManager.removeSchedule(schedule)
                            showDeleteConfirmation = null
                        } catch (e: Exception) {
                            Log.e("SchedulePage", "Error removing schedule: ${e.message}", e)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirmation = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showScheduleDialog) {
        EnhancedScheduleDialog(
            onDismiss = { showScheduleDialog = false },
            onSaveSchedule = { scheduleString ->
                try {
                    deviceManager.saveSchedule(scheduleString)
                    Toast.makeText(context, "Schedule created", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("SchedulePage", "Error saving schedule: ${e.message}", e)
                }
            }
        )
    }
}


@Composable
fun ScheduleItem(
    schedule: String,
    onDelete: () -> Unit
) {
    val deviceType = when {
        schedule.contains("(Light)") -> "Light"
        schedule.contains("(Plug)") -> "Plug"
        schedule.contains("(Camera)") -> "Camera"
        schedule.contains("(Sensor)") -> "Sensor"
        else -> ""
    }

    val deviceIcon = when(deviceType) {
        "Light" -> Icons.Default.Lightbulb
        "Plug" -> Icons.Default.PowerSettingsNew
        "Camera" -> Icons.Default.Videocam
        "Sensor" -> Icons.Default.Sensors
        else -> Icons.Default.Devices
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon representing the device type
            Icon(
                imageVector = deviceIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.secondary
            )

            // Schedule description
            Text(
                text = schedule,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            // Delete button
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Schedule",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedScheduleDialog(
    onDismiss: () -> Unit,
    deviceName: String = "",
    deviceType: String = "Light",
    onSaveSchedule: (String) -> Unit = {}
) {
    // Device type and lists
    var selectedDeviceType by remember { mutableStateOf(deviceType) }
    var devicesList by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedDevice by remember { mutableStateOf(deviceName) }

    // Time selection
    var selectedHour by remember { mutableStateOf(8) }
    var selectedMinute by remember { mutableStateOf(0) }
    var selectedAmPm by remember { mutableStateOf("AM") }

    // Action selection
    var selectedAction by remember { mutableStateOf("ON") }
    var actionsList by remember { mutableStateOf<List<String>>(listOf("ON", "OFF")) }

    // Color selection for lights
    var selectedColor by remember { mutableStateOf(Color.White) }
    var showColorPicker by remember { mutableStateOf(false) }

    // Brightness for lights
    var brightness by remember { mutableStateOf(80f) }

    // Days selection
    var selectedDays by remember { mutableStateOf(setOf("MON", "TUE", "WED", "THU", "FRI")) }

    // Error handling
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // UI state
    var deviceTypeExpanded by remember { mutableStateOf(false) }
    var deviceListExpanded by remember { mutableStateOf(false) }
    var hourExpanded by remember { mutableStateOf(false) }
    var minuteExpanded by remember { mutableStateOf(false) }
    var amPmExpanded by remember { mutableStateOf(false) }
    var actionExpanded by remember { mutableStateOf(false) }

    // Color picker state - use regular mutableStateOf instead of mutableFloatStateOf
    var red by remember { mutableStateOf(selectedColor.red) }
    var green by remember { mutableStateOf(selectedColor.green) }
    var blue by remember { mutableStateOf(selectedColor.blue) }

    // Load device lists based on type
    LaunchedEffect(selectedDeviceType) {
        devicesList = when (selectedDeviceType) {
            "Light" -> DeviceManager.knownLights
            "Plug" -> DeviceManager.knownPlugs
            "Camera" -> DeviceManager.knownCameras
            "Sensor" -> DeviceManager.knownSensors
            else -> emptyList()
        }

        // Update available actions based on device type
        actionsList = when (selectedDeviceType) {
            "Light" -> listOf("ON", "OFF", "SET_COLOR", "SET_BRIGHTNESS")
            "Plug" -> listOf("ON", "OFF")
            "Camera" -> listOf("ON", "OFF", "CAPTURE")
            "Sensor" -> listOf("ALERT_ON", "ALERT_OFF", "CHECK")
            else -> listOf("ON", "OFF")
        }

        // If no device is selected and list isn't empty, select the first one
        if (selectedDevice.isEmpty() && devicesList.isNotEmpty()) {
            selectedDevice = devicesList.first()
        }

        // Reset to ON if current action is not valid for this device type
        if (selectedAction !in actionsList) {
            selectedAction = actionsList.first()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Create Schedule",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Device Type Selection
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "1. Select Device Type",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        ExposedDropdownMenuBox(
                            expanded = deviceTypeExpanded,
                            onExpandedChange = { deviceTypeExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedDeviceType,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Device Type") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = when(selectedDeviceType) {
                                            "Light" -> Icons.Default.Lightbulb
                                            "Plug" -> Icons.Default.PowerSettingsNew
                                            "Camera" -> Icons.Default.Videocam
                                            "Sensor" -> Icons.Default.Sensors
                                            else -> Icons.Default.Settings
                                        },
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = deviceTypeExpanded)
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = deviceTypeExpanded,
                                onDismissRequest = { deviceTypeExpanded = false }
                            ) {
                                listOf("Light", "Plug", "Camera", "Sensor").forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = when(type) {
                                                    "Light" -> Icons.Default.Lightbulb
                                                    "Plug" -> Icons.Default.PowerSettingsNew
                                                    "Camera" -> Icons.Default.Videocam
                                                    "Sensor" -> Icons.Default.Sensors
                                                    else -> Icons.Default.Settings
                                                },
                                                contentDescription = null
                                            )
                                        },
                                        onClick = {
                                            selectedDeviceType = type
                                            deviceTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Device Selection
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "2. Select Device",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (devicesList.isEmpty()) {
                            Text(
                                text = "No ${selectedDeviceType.lowercase()} devices available. Please add a device first.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            ExposedDropdownMenuBox(
                                expanded = deviceListExpanded,
                                onExpandedChange = { deviceListExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedDevice,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Select Device") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = deviceListExpanded)
                                    },
                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )

                                ExposedDropdownMenu(
                                    expanded = deviceListExpanded,
                                    onDismissRequest = { deviceListExpanded = false }
                                ) {
                                    devicesList.forEach { device ->
                                        DropdownMenuItem(
                                            text = { Text(device) },
                                            onClick = {
                                                selectedDevice = device
                                                deviceListExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Time Selection
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "3. Set Time",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Hour selection
                            ExposedDropdownMenuBox(
                                expanded = hourExpanded,
                                onExpandedChange = { hourExpanded = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedHour.toString(),
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Hour") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = hourExpanded)
                                    },
                                    modifier = Modifier.menuAnchor()
                                )

                                ExposedDropdownMenu(
                                    expanded = hourExpanded,
                                    onDismissRequest = { hourExpanded = false }
                                ) {
                                    (1..12).forEach { hour ->
                                        DropdownMenuItem(
                                            text = { Text(hour.toString()) },
                                            onClick = {
                                                selectedHour = hour
                                                hourExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Minute selection
                            ExposedDropdownMenuBox(
                                expanded = minuteExpanded,
                                onExpandedChange = { minuteExpanded = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = String.format("%02d", selectedMinute),
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Minute") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = minuteExpanded)
                                    },
                                    modifier = Modifier.menuAnchor()
                                )

                                ExposedDropdownMenu(
                                    expanded = minuteExpanded,
                                    onDismissRequest = { minuteExpanded = false }
                                ) {
                                    // Allow selection of any minute 0-59
                                    (0..59).forEach { minute ->
                                        DropdownMenuItem(
                                            text = { Text(String.format("%02d", minute)) },
                                            onClick = {
                                                selectedMinute = minute
                                                minuteExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // AM/PM selection
                            ExposedDropdownMenuBox(
                                expanded = amPmExpanded,
                                onExpandedChange = { amPmExpanded = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedAmPm,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("AM/PM") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = amPmExpanded)
                                    },
                                    modifier = Modifier.menuAnchor()
                                )

                                ExposedDropdownMenu(
                                    expanded = amPmExpanded,
                                    onDismissRequest = { amPmExpanded = false }
                                ) {
                                    listOf("AM", "PM").forEach { period ->
                                        DropdownMenuItem(
                                            text = { Text(period) },
                                            onClick = {
                                                selectedAmPm = period
                                                amPmExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Display selected time
                        Text(
                            text = "Selected time: ${selectedHour}:${String.format("%02d", selectedMinute)} ${selectedAmPm}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }

                // Days Selection
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "4. Select Days",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // Quick selection buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    selectedDays = setOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
                                },
                                modifier = Modifier.weight(1f).padding(end = 4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedDays.size == 7)
                                        MaterialTheme.colorScheme.secondary
                                    else
                                        MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text("Every Day", fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    selectedDays = setOf("MON", "TUE", "WED", "THU", "FRI")
                                },
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedDays == setOf("MON", "TUE", "WED", "THU", "FRI"))
                                        MaterialTheme.colorScheme.secondary
                                    else
                                        MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text("Weekdays", fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    selectedDays = setOf("SAT", "SUN")
                                },
                                modifier = Modifier.weight(1f).padding(start = 4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedDays == setOf("SAT", "SUN"))
                                        MaterialTheme.colorScheme.secondary
                                    else
                                        MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text("Weekend", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Individual day checkboxes
                        val dayMap = listOf(
                            "MON" to "Monday",
                            "TUE" to "Tuesday",
                            "WED" to "Wednesday",
                            "THU" to "Thursday",
                            "FRI" to "Friday",
                            "SAT" to "Saturday",
                            "SUN" to "Sunday"
                        )

                        Column {
                            dayMap.forEach { (code, name) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = selectedDays.contains(code),
                                        onCheckedChange = { isChecked ->
                                            selectedDays = if (isChecked) {
                                                selectedDays + code
                                            } else {
                                                selectedDays - code
                                            }
                                        }
                                    )
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Action Selection
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "5. Select Action",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        ExposedDropdownMenuBox(
                            expanded = actionExpanded,
                            onExpandedChange = { actionExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = when(selectedAction) {
                                    "ON" -> "Turn On"
                                    "OFF" -> "Turn Off"
                                    "SET_COLOR" -> "Set Color"
                                    "SET_BRIGHTNESS" -> "Set Brightness"
                                    "CAPTURE" -> "Capture Image"
                                    "ALERT_ON" -> "Enable Alerts"
                                    "ALERT_OFF" -> "Disable Alerts"
                                    "CHECK" -> "Check Status"
                                    else -> selectedAction
                                },
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Action") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = when(selectedAction) {
                                            "ON" -> Icons.Default.PowerSettingsNew
                                            "OFF" -> Icons.Default.Power
                                            "SET_COLOR" -> Icons.Default.Palette
                                            "SET_BRIGHTNESS" -> Icons.Default.LightMode
                                            "CAPTURE" -> Icons.Default.Videocam
                                            "ALERT_ON", "ALERT_OFF", "CHECK" -> Icons.Default.Sensors
                                            else -> Icons.Default.Settings
                                        },
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = actionExpanded)
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = actionExpanded,
                                onDismissRequest = { actionExpanded = false }
                            ) {
                                actionsList.forEach { action ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(when(action) {
                                                "ON" -> "Turn On"
                                                "OFF" -> "Turn Off"
                                                "SET_COLOR" -> "Set Color"
                                                "SET_BRIGHTNESS" -> "Set Brightness"
                                                "CAPTURE" -> "Capture Image"
                                                "ALERT_ON" -> "Enable Alerts"
                                                "ALERT_OFF" -> "Disable Alerts"
                                                "CHECK" -> "Check Status"
                                                else -> action
                                            })
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = when(action) {
                                                    "ON" -> Icons.Default.PowerSettingsNew
                                                    "OFF" -> Icons.Default.Power
                                                    "SET_COLOR" -> Icons.Default.Palette
                                                    "SET_BRIGHTNESS" -> Icons.Default.LightMode
                                                    "CAPTURE" -> Icons.Default.Videocam
                                                    "ALERT_ON", "ALERT_OFF", "CHECK" -> Icons.Default.Sensors
                                                    else -> Icons.Default.Settings
                                                },
                                                contentDescription = null
                                            )
                                        },
                                        onClick = {
                                            selectedAction = action
                                            actionExpanded = false
                                            if (action == "SET_COLOR") {
                                                showColorPicker = true
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        // Show color picker if SET_COLOR is selected
                        if (selectedAction == "SET_COLOR" && selectedDeviceType == "Light") {
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Select Color",
                                style = MaterialTheme.typography.titleSmall
                            )

                            // Color preview
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .background(selectedColor)
                                    .border(1.dp, Color.Gray)
                                    .clickable { showColorPicker = true }
                            )

                            // Common colors
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ColorButton(Color.Red, selectedColor, onColorSelected = {
                                    selectedColor = it
                                }) {}
                                ColorButton(Color.Green, selectedColor, onColorSelected = {
                                    selectedColor = it
                                }) {}
                                ColorButton(Color.Blue, selectedColor, onColorSelected = {
                                    selectedColor = it
                                }) {}
                                ColorButton(Color.Yellow, selectedColor, onColorSelected = {
                                    selectedColor = it
                                }) {}
                                ColorButton(Color.White, selectedColor, onColorSelected = {
                                    selectedColor = it
                                }) {}
                            }
                        }

                        // Show brightness slider if SET_BRIGHTNESS is selected
                        if (selectedAction == "SET_BRIGHTNESS" && selectedDeviceType == "Light") {
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Set Brightness: ${brightness.toInt()}%",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LightMode,
                                    contentDescription = "Low brightness",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Slider(
                                    value = brightness,
                                    onValueChange = { brightness = it },
                                    valueRange = 0f..100f,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 8.dp)
                                )

                                Icon(
                                    imageVector = Icons.Default.LightMode,
                                    contentDescription = "High brightness",
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }

                // Error message (if any)
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Validate inputs
                    when {
                        selectedDevice.isEmpty() && devicesList.isEmpty() -> {
                            errorMessage = "Please add devices first"
                            return@Button
                        }
                        selectedDevice.isEmpty() -> {
                            errorMessage = "Please select a device"
                            return@Button
                        }
                        selectedDays.isEmpty() -> {
                            errorMessage = "Please select at least one day"
                            return@Button
                        }
                        else -> {
                            // Format days string
                            val daysString = if (selectedDays.size == 7) {
                                "Every day"
                            } else {
                                selectedDays.joinToString(", ")
                            }

                            // Format action string and value
                            val (actionString, actionValue) = when (selectedAction) {
                                "ON" -> Pair("turn ON", "")
                                "OFF" -> Pair("turn OFF", "")
                                "SET_COLOR" -> {
                                    val hexColor = "#" + Integer.toHexString(selectedColor.toArgb()).substring(2)
                                    Pair("set color to $hexColor", hexColor)
                                }
                                "SET_BRIGHTNESS" ->
                                    Pair("set brightness to ${brightness.toInt()}%", brightness.toInt().toString())
                                "CAPTURE" -> Pair("capture image", "")
                                "ALERT_ON" -> Pair("enable alerts", "")
                                "ALERT_OFF" -> Pair("disable alerts", "")
                                "CHECK" -> Pair("check status", "")
                                else -> Pair(selectedAction, "")
                            }

                            // Format time string
                            val timeString = String.format("%d:%02d %s", selectedHour, selectedMinute, selectedAmPm)

                            // Create schedule string
                            val scheduleString = "$selectedDevice ($selectedDeviceType): $actionString at $timeString on $daysString"

                            // Save and dismiss
                            onSaveSchedule(scheduleString)
                            onDismiss()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Schedule")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text("Cancel")
            }
        }
    )

    // Full color picker dialog
    if (showColorPicker) {
        AlertDialog(
            onDismissRequest = { showColorPicker = false },
            title = { Text("Select Color") },
            text = {
                Column {
                    // Color preview
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(Color(red, green, blue))
                            .border(1.dp, Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // RGB sliders
                    Text("Red")
                    Slider(
                        value = red,
                        onValueChange = { red = it },
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Red,
                            activeTrackColor = Color.Red.copy(alpha = 0.5f)
                        )
                    )

                    Text("Green")
                    Slider(
                        value = green,
                        onValueChange = { green = it },
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Green,
                            activeTrackColor = Color.Green.copy(alpha = 0.5f)
                        )
                    )

                    Text("Blue")
                    Slider(
                        value = blue,
                        onValueChange = { blue = it },
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Blue,
                            activeTrackColor = Color.Blue.copy(alpha = 0.5f)
                        )
                    )

                    // Hex color code
                    val hexColor = "#" + Integer.toHexString(Color(red, green, blue).toArgb()).substring(2).uppercase()
                    Text(
                        text = "Hex: $hexColor",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Preset colors
                    Text(
                        text = "Presets",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    // Simple grid of preset colors
                    val presetColors = listOf(
                        Color.Red, Color.Green, Color.Blue, Color.Yellow,
                        Color.Cyan, Color.Magenta, Color.White, Color(0xFFFF8C00), // Orange
                        Color(0xFF800080), Color(0xFF98FB98), Color(0xFF87CEEB), Color(0xFFFFB6C1) // Purple, Pale green, Sky blue, Pink
                    )

                    // Create a row-based grid
                    Column {
                        for (i in 0 until presetColors.size step 4) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                for (j in 0 until minOf(4, presetColors.size - i)) {
                                    val color = presetColors[i + j]
                                    Box(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .size(32.dp)
                                            .background(color, CircleShape)
                                            .border(
                                                width = 2.dp,
                                                color = if (Color(red, green, blue) == color)
                                                    MaterialTheme.colorScheme.secondary else Color.Transparent,
                                                shape = CircleShape
                                            )
                                            .clickable {
                                                red = color.red
                                                green = color.green
                                                blue = color.blue
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    selectedColor = Color(red, green, blue)
                    showColorPicker = false
                }) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { showColorPicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun DeviceListItem(
    deviceName: String,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    isSelected: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = deviceName,
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun MyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    onTurnLightOn: () -> Unit,
    onTurnLightOff: () -> Unit,
    onBrightnessChange: (Float) -> Unit,
    setBrightness: (Float) -> Unit,
    onSetColor: (Color) -> Unit,
    sessionManager: SessionManager,       // Add SessionManager parameter
    deviceManager: PersistentDeviceManager, // Add DeviceManager parameter
    deviceController: DeviceController
) {
    NavHost(
        navController = navController,
        startDestination = if (sessionManager.isLoggedIn() && sessionManager.isRememberMeEnabled()) "home" else "first",
        modifier = modifier
    ) {
        composable("shelly") {
            ShellyScreen(
                navController = navController,
                wifiScanner = sharedViewModel.wifiScanner,
                logs = sharedViewModel.logs
            )
        }
        composable("first") {
            FirstScreen(
                navController = navController
            )
        }
        composable("wififence") {
            WifiMonitorScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }

        // Email verification with code
        composable(
            route = "verify-email/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            CodeVerificationScreen(
                navController = navController,
                email = email,
                verificationType = VerificationType.EMAIL_VERIFICATION
            )
        }

        // Password reset routes
        composable("forgot-password") {
            ForgotPasswordScreen(navController)
        }
        composable("suggestions") {
            SuggestionsScreen(navController)
        }
        composable(
            route = "reset-password-verify/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            CodeVerificationScreen(
                navController = navController,
                email = email,
                verificationType = VerificationType.PASSWORD_RESET
            )
        }

        composable(
            route = "reset-password-confirm/{email}/{code}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                },
                navArgument("code") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val code = backStackEntry.arguments?.getString("code") ?: ""
            ResetPasswordConfirmScreen(
                navController = navController,
                email = email,
                verificationCode = code
            )
        }

        // Account deletion route
        composable(
            route = "delete-account/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            CodeVerificationScreen(
                navController = navController,
                email = email,
                verificationType = VerificationType.ACCOUNT_DELETION,
                sessionManager = sessionManager,      // Add these parameters
                deviceManager = deviceManager         // Add these parameters
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                sessionManager = sessionManager,
                deviceController = deviceController,
                onTurnLightOn = onTurnLightOn,
                onTurnLightOff = onTurnLightOff
            )
        }
        composable("addDevice") {
            AddDeviceScreen(
                onDeviceAdded = { navController.popBackStack() },
                navController = navController
            )
        }
        composable("lights") {
            LightsScreen(
                navController = navController,
                onTurnLightOn = onTurnLightOn,
                onTurnLightOff = onTurnLightOff,
                onBrightnessChange = { brightness -> setBrightness(brightness) },
                onSetColor = onSetColor
            )
        }
        composable("plugs") {
            val context = LocalContext.current
            val deviceController = DeviceController(context)
            PlugsScreen(navController = navController, deviceController = deviceController)
        }
        composable("cameras") {
            CamerasScreen(navController = navController)
        }
        composable("sensors") {
            SensorsScreen(navController = navController)
        }
        composable("schedule") {
            SchedulePage(navController = navController)
        }
        composable("settings") {
            SettingsScreen(
                navController = navController,
                sessionManager = sessionManager, // Pass SessionManager to SettingsScreen
                deviceManager = deviceManager    // Pass DeviceManager to SettingsScreen
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    LoginScreen(navController)
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController() // Mock NavController for preview
    val context = LocalContext.current
    val mockSessionManager = remember { SessionManager(context) } // Create mock SessionManager for preview
    val mockDeviceController = remember { DeviceController(context) }

    HomeScreen(
        navController = navController,
        sessionManager = mockSessionManager,
        deviceController = mockDeviceController,
        onTurnLightOn = {}, // 🔧 pass no-op lambda
        onTurnLightOff = {} // 🔧 pass no-op lambda
    )
}