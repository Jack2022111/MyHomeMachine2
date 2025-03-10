package com.example.myhomemachine

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myhomemachine.data.DeviceManager
import com.example.myhomemachine.network.AuthViewModel
import com.example.myhomemachine.ui.theme.MyHomeMachineTheme
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.math.roundToInt

private const val LIFX_API_TOKEN = "c30381e0c360262972348a08fdda96e118d69ded53ec34bd1e06c24bd37fc247"
private const val LIFX_SELECTOR = "all" // Can be "label:your_light_name" or "all"

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
    private lateinit var geofencingClient: GeofencingClient
    private val logs = mutableStateListOf<String>() // Store logs for UI

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        geofencingClient = LocationServices.getGeofencingClient(this)
        checkAndRequestPermissions()

        val sharedViewModel: SharedViewModel by viewModels()
        sharedViewModel.initialize(this)

        // Register a receiver to capture geofence events and update UI logs.
        val geofenceReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val message = intent?.getStringExtra("message") ?: "No message"
                logs.add(message) // ✅ Update local logs list
                sharedViewModel.logs.add(message) // ✅ Also update ViewModel logs
                Log.d("Geofence", message)
            }
        }
        registerReceiver(geofenceReceiver, IntentFilter("com.example.myhomemachine.GEOFENCE_EVENT"))

        setContent {
            MyHomeMachineTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        sharedViewModel = sharedViewModel,
                        logs = logs, // ✅ Pass logs correctly
                        onTurnLightOn = { turnLightOn() },
                        onTurnLightOff = { turnLightOff() },
                        onBrightnessChange = { brightness -> setBrightness(brightness) },
                        setBrightness = { brightness -> this.setBrightness(brightness) },
                        onSetColor = { color -> setColor(color) },
                        onSetGeofence = { setGeofenceAtCurrentLocation() }  // Pass the lambda here
                    )
                }
            }
        }
    }

    private fun turnLightOn() {
        val apiService = RetrofitClient.instance
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
            } catch (e: Exception) {
                Log.e("LIFX", "Failed to turn light on", e)
            }
        }
    }

    private fun turnLightOff() {
        val apiService = RetrofitClient.instance
        val body = LightState(power = "off", color = lastColor)

        lifecycleScope.launch {
            try {
                apiService.setLightState(
                    selector = LIFX_SELECTOR,
                    authHeader = "Bearer $LIFX_API_TOKEN",
                    body = body
                )
                isLightOn = false // ✅ Make sure this updates correctly
                Log.d("LIFX", "Light turned OFF")
            } catch (e: Exception) {
                Log.e("LIFX", "Failed to turn light off", e)
            }
        }
    }

    private fun setBrightness(brightness: Float) {
        currentBrightness = brightness // Store brightness

        val apiService = RetrofitClient.instance
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

        val apiService = RetrofitClient.instance
        val body = LightState(color = lastColor, power = "on")

        lifecycleScope.launch {
            try {
                apiService.setLightState(
                    selector = LIFX_SELECTOR,
                    authHeader = "Bearer $LIFX_API_TOKEN",
                    body = body
                )
                Log.d("LIFX", "Color set to HSB: ${hsb[0]}, ${hsb[1]}, ${hsb[2]}")
            } catch (e: Exception) {
                Log.e("LIFX", "Failed to set color", e)
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionsNeeded = mutableListOf<String>()

        // Check for foreground location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Check for background location permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsNeeded.toTypedArray(),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun createGeofence(latitude: Double, longitude: Double, radius: Float): Geofence {
        return Geofence.Builder()
            .setRequestId("HOME_GEOFENCE")
            .setCircularRegion(latitude, longitude, radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()
    }

    private fun createGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()
    }

    private fun addGeofence(latitude: Double, longitude: Double, radius: Float) {
        val geofence = createGeofence(latitude, longitude, radius)
        val geofencingRequest = createGeofencingRequest(geofence)

        // Use a PendingIntent pointing to your BroadcastReceiver
        val geofencePendingIntent: PendingIntent by lazy {
            val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
            PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                .addOnSuccessListener {
                    Log.d("Geofence", "Geofence added successfully at: ($latitude, $longitude)")
                }
                .addOnFailureListener { e ->
                    Log.e("Geofence", "Failed to add geofence: ${e.message}")
                }
        }
    }

    /*
    private fun setGeofenceAtCurrentLocation(radius: Float = 10f) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Check if location permission is NOT granted, then return early.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Geofence", "Location permission not granted.")
            return
        }
        // If permissions are granted, proceed to get the location.
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    addGeofence(location.latitude, location.longitude, radius)
                } else {
                    Log.e("Geofence", "Current location is null. Ensure location is enabled on your device.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Geofence", "Failed to get current location: ${e.message}")
            }
    }

     */
    private fun setGeofenceAtCurrentLocation(radius: Float = 10f) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Geofence", "Location permission not granted.")
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    addGeofence(location.latitude, location.longitude, radius)
                } else {
                    Log.e("Geofence", "Current location is null. Ensure location is enabled on your device.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Geofence", "Failed to get current location: ${e.message}")
            }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeofenceLogsScreen(
    logs: MutableList<String>,  // ✅ Now this directly receives updates
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Geofence Logs") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                .padding(16.dp)
        ) {
            Text(
                text = "Geofence Logs",
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
                    .verticalScroll(rememberScrollState()) // Make logs scrollable
            ) {
                if (logs.isEmpty()) {
                    Text("No logs available", style = MaterialTheme.typography.bodyMedium)
                } else {
                    logs.forEach { log ->
                        Text(
                            text = log,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Clear Logs Button
            Button(
                onClick = { logs.clear() }, // ✅ This will now clear logs properly
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = "Clear Logs")
            }
        }
    }
}



class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent != null && !geofencingEvent.hasError()) {
            val message = when (geofencingEvent.geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> "Entered geofence"
                Geofence.GEOFENCE_TRANSITION_EXIT -> "Exited geofence"
                else -> "Unknown geofence event"
            }

            Log.d("Geofence", message)

            // Broadcast the event so that the UI can update the logs
            val broadcastIntent = Intent("com.example.myhomemachine.GEOFENCE_EVENT")
            broadcastIntent.putExtra("message", message)
            context.sendBroadcast(broadcastIntent)
        } else {
            Log.e("Geofence", "Error receiving geofence event")
        }
    }
}


// DeviceController class to manage the Shelly Plug
class DeviceController {
    private val client = OkHttpClient()
    //private val shellyIpAddress = "http://10.5.2.30" // Shelly plug IP (this is specifically for b2 wifi network it will change later)

    // Function to turn on the Shelly Plug
    fun turnOnPlug() {
        toggleShellyRelay(true)
    }

    // Turn off the Shelly Plug
    fun turnOffPlug() {
        toggleShellyRelay(false)
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

@Composable
fun FirstScreen(
    navController: NavHostController,
    onSetGeofence: () -> Unit,
    logs: List<String>
) {
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
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(200.dp)
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

            // Bottom section with buttons
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
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
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
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Continue as Guest",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // Button to set the geofence and navigate to the logs screen
                Button(
                    onClick = onSetGeofence,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Set Geofence")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun ShellyScreen(
    navController: NavHostController,
    wifiScanner: WifiScanner,
    logs: MutableList<String>
) {
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
            // Top Section (Placeholder for Logo or Title)
            Text(
                text = "Shelly Plug Setup",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
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
                        .verticalScroll(rememberScrollState()) // Make logs scrollable
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
                        containerColor = MaterialTheme.colorScheme.secondary
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
                    containerColor = MaterialTheme.colorScheme.secondary
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

    // State variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

                    // Forgot password button
                    TextButton(
                        onClick = { navController.navigate("forgot-password") },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Forgot Password?")
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceScreen(onDeviceAdded: () -> Unit, navController: NavHostController) {
    // State variables
    var selectedDeviceType by remember { mutableStateOf("") }
    var selectedDeviceName by remember { mutableStateOf<String?>(null) }
    var customDeviceName by remember { mutableStateOf("") }
    var typeExpanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Define device types and associated device names
    val deviceTypes = listOf("Camera", "Light", "Plug", "Sensor")
    val devicesByType = mapOf(
        "Camera" to listOf("RaspberryPiCamera"),
        "Light" to listOf("LIFX Smart Light"),  // Only one smart light option now
        "Plug" to listOf("Shelly Smart Plug"),  // Only one plug option now
        "Sensor" to listOf("ThermoSync_Q5", "BreezeIQ_G87")
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Device") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Device Type Selection Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "1. Select Device Type",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

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
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                            },
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

            // Device Selection Card
            if (selectedDeviceType.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "2. Select Available Device",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

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
                                    onClick = { selectedDeviceName = device }
                                )
                                Text(device)
                            }
                        }
                    }
                }
            }

            // Custom Name Card
            if (selectedDeviceName != null) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "3. Set Custom Name (Optional)",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = customDeviceName,
                            onValueChange = { customDeviceName = it },
                            label = { Text("Custom Name") },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(selectedDeviceName ?: "") }
                        )
                    }
                }
            }


            // shelly device add screen button
            Column(
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("shelly") }, // Replace "shelly" with "main_screen"
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .animateContentSize(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    ),
                    enabled = selectedDeviceType == "Plug" && selectedDeviceName == "Shelly Smart Plug" // Only enable when Shelly Smart Plug is selected
                ) {
                    Text("Navigate to add Shelly Smart Plug")
                }
            }

            // Add Device Button
            if (selectedDeviceName != null) {
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Device")
                }
            }
        }
    }

    // Confirmation Dialog
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
                        DeviceManager.addDevice(deviceName, selectedDeviceType)
                        showConfirmDialog = false
                        //navController.navigate("home") {
                        //    popUpTo("home") { inclusive = false }
                        //}
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

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
                .url("http://10.5.2.37:5000/toggle_motion/$device")
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

@Composable
private fun EnhancedScheduleDialog(onDismiss: () -> Unit) {
    var selectedTime by remember { mutableStateOf("") }
    var selectedDays by remember { mutableStateOf(setOf<String>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Schedule") },
        text = {
            Column {
                Text("Select time and days for the schedule")
                // Add time picker and day selection here
                // This is a placeholder for later milestone- you would want to add actual time/day selection UI
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Plugs section
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlugsScreen(navController: NavHostController, deviceController: DeviceController) {
    var knownPlugs = DeviceManager.knownPlugs
    var selectedPlug by remember { mutableStateOf<String?>(null) }
    var isPlugOn by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var showScheduleDialog by remember { mutableStateOf(false) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedPlug ?: "Choose a plug",
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
                            knownPlugs.forEach { plug ->
                                DropdownMenuItem(
                                    text = { Text(plug) },
                                    onClick = {
                                        selectedPlug = plug
                                        expanded = false
                                    }
                                )
                            }
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
                                    deviceController.turnOffPlug()  // Turn off the plug
                                } else {
                                    deviceController.turnOnPlug()  // Turn on the plug
                                }
                                isPlugOn = !isPlugOn  // Update the state
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamerasScreen(navController: NavHostController) {
    var knownCameras = DeviceManager.knownCameras
    var selectedCamera by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    var motionDetectionLight by remember { mutableStateOf(true) }
    var motionDetectionPlug by remember { mutableStateOf(true) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCamera ?: "Choose a camera",
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
                            knownCameras.forEach { camera ->
                                DropdownMenuItem(
                                    text = { Text(camera) },
                                    onClick = {
                                        selectedCamera = camera
                                        expanded = false
                                    }
                                )
                            }
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
                        // WebView for camera stream
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    settings.javaScriptEnabled = true
                                    webViewClient = WebViewClient()
                                    loadUrl("http://10.5.2.37:5000/stream")  // Replace with Raspberry Pi IP
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Toggle Motion Detection for Lights
                        Button(
                            onClick = {
                                toggleMotionDetection("light") {
                                    motionDetectionLight = !motionDetectionLight
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (motionDetectionLight) "Disable Light Motion Detection" else "Enable Light Motion Detection")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Toggle Motion Detection for Plug
                        Button(
                            onClick = {
                                toggleMotionDetection("plug") {
                                    motionDetectionPlug = !motionDetectionPlug
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (motionDetectionPlug) "Disable Plug Motion Detection" else "Enable Plug Motion Detection")
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorsScreen(navController: NavHostController) {
    var knownSensors = DeviceManager.knownSensors
    var selectedSensor by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    // Example data for temperature and air quality history
    val temperatureHistory = listOf(72, 74, 76, 75, 73)
    val airQualityHistory = listOf(42, 39, 47, 40, 43)

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
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

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedSensor ?: "Choose a sensor",
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
                            knownSensors.forEach { sensor ->
                                DropdownMenuItem(
                                    text = { Text(sensor) },
                                    onClick = {
                                        selectedSensor = sensor
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sensor Data Display
            if (selectedSensor != null) {
                when (selectedSensor) {
                    "Temperature Sensor" -> TemperatureDisplay(temperatureHistory)
                    "Air Quality Sensor" -> AirQualityDisplay(airQualityHistory)
                }
            }
        }
    }
}

@Composable
fun TemperatureDisplay(temperatureHistory: List<Int>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Temperature History",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            temperatureHistory.forEachIndexed { index, temp ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Time ${index + 1}")
                    Text("$temp°F", style = MaterialTheme.typography.titleMedium)
                }
                if (index < temperatureHistory.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
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

@Composable
fun SchedulePage(navController: NavHostController) {
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
                    text = "Device Schedules",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Schedule display card
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
                    if (DeviceManager.schedules.isEmpty()) {
                        // Message for no schedules
                        Text(
                            text = "No current schedules available.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        // Display each schedule
                        DeviceManager.schedules.forEach { schedule ->
                            Text(text = schedule, style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Back button at the bottom
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Back")
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
    logs: MutableList<String>,  // ✅ Pass logs here
    onTurnLightOn: () -> Unit,
    onTurnLightOff: () -> Unit,
    onBrightnessChange: (Float) -> Unit,
    setBrightness: (Float) -> Unit,
    onSetColor: (Color) -> Unit,
    onSetGeofence: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "first",
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
                navController = navController,
                // When clicking "Set Geofence", call setGeofenceAtCurrentLocation() and then navigate to logs screen.
                onSetGeofence = {
                    onSetGeofence()
                    navController.navigate("geofence_logs")
                },
                logs = sharedViewModel.logs
            )
        }
        composable("geofence_logs") {
            GeofenceLogsScreen(logs = logs, onBack = { navController.popBackStack() }) // ✅ Logs update dynamically
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
                verificationType = VerificationType.ACCOUNT_DELETION
            )
        }

        composable("home") {
            HomeScreen(navController = navController)
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
            val deviceController = DeviceController()
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
            SettingsScreen(navController = navController)
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
    HomeScreen(navController = navController)
}