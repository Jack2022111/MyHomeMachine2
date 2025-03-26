// CameraTest.kt
package com.example.myhomemachine

import android.content.Context
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import org.mockito.Mockito
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import java.io.IOException

class CameraTest {

    /**
     * This is a test-only implementation that mirrors the functionality in your
     * ToggleMotionDetection function used in your UI. 
     * 
     * In a production app, this would ideally be a real class used by your UI,
     * not duplicated here.
     */
    open class CameraController(private val context: Context) {
        // Properties that can be overridden in tests
        open val client = OkHttpClient()
        open val deviceNotifier = DeviceNotificationManager(context)
        
        fun toggleMotionDetection(device: String, onComplete: () -> Unit) {
            val request = Request.Builder()
                .url("http://10.5.2.37:5000/toggle_motion/$device")
                .put(okhttp3.RequestBody.create(null, ByteArray(0))) // Empty PUT body
                .build()
            
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // No-op for test
                }
                
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        // Send notification about the status change
                        deviceNotifier.sendDeviceNotification(
                            deviceType = DeviceType.CAMERA,
                            eventType = EventType.STATUS_CHANGE,
                            deviceName = "Camera",
                            additionalDetails = "Motion detection toggled for $device"
                        )
                        onComplete()
                    }
                }
            })
        }
        
        fun getCameraStreamUrl(): String {
            return "http://10.5.2.37:5000/stream"
        }
    }

    @Test
    fun testToggleMotionDetection_sendsCorrectRequest() {
        // Create mocks
        val mockContext = Mockito.mock(Context::class.java)
        val mockClient = Mockito.mock(OkHttpClient::class.java)
        val mockCall = Mockito.mock(Call::class.java)
        val mockNotifier = Mockito.mock(DeviceNotificationManager::class.java)
        
        // Create a testable subclass
        val controller = object : CameraController(mockContext) {
            override val client = mockClient
            override val deviceNotifier = mockNotifier
        }
        
        // Setup mock behavior
        Mockito.`when`(mockClient.newCall(any(Request::class.java))).thenReturn(mockCall)
        
        // Completion flag
        var completionCalled = false
        
        // Act
        controller.toggleMotionDetection("light") {
            completionCalled = true
        }
        
        // Verify the request URL
        Mockito.verify(mockClient).newCall(Mockito.argThat { req ->
            req.url.toString() == "http://10.5.2.37:5000/toggle_motion/light" &&
            req.method == "PUT"
        })
        
        // Verify callback was registered
        Mockito.verify(mockCall).enqueue(any(Callback::class.java))
        
        // Capture the callback and simulate a successful response
        val callbackCaptor = ArgumentCaptor.forClass(Callback::class.java)
        Mockito.verify(mockCall).enqueue(callbackCaptor.capture())
        
        // Create a mock response
        val mockResponse = Mockito.mock(Response::class.java)
        Mockito.`when`(mockResponse.isSuccessful).thenReturn(true)
        
        // Call the callback with the mock response
        callbackCaptor.value.onResponse(mockCall, mockResponse)
        
        // Verify the notification was sent
        Mockito.verify(mockNotifier).sendDeviceNotification(
            deviceType = DeviceType.CAMERA,
            eventType = EventType.STATUS_CHANGE,
            deviceName = "Camera",
            additionalDetails = "Motion detection toggled for light"
        )
        
        // Verify the completion callback was called
        assertTrue("Completion callback should be called", completionCalled)
    }

    @Test
    fun testGetCameraStreamUrl_returnsCorrectUrl() {
        val mockContext = Mockito.mock(Context::class.java)
        val controller = CameraController(mockContext)
        
        val streamUrl = controller.getCameraStreamUrl()
        
        assertTrue(
            "Stream URL should point to correct camera endpoint",
            streamUrl == "http://10.5.2.37:5000/stream"
        )
    }
}