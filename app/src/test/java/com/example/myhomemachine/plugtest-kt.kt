//// PlugTest.kt
//package com.example.myhomemachine
//
//import android.content.Context
//import android.content.Intent
//import okhttp3.Call
//import okhttp3.Callback
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.Response
//import org.junit.Test
//import org.mockito.Mockito
//import org.mockito.ArgumentMatchers.any
//import org.mockito.ArgumentMatchers.anyString
//import org.mockito.ArgumentMatchers.eq
//import java.io.IOException
//
//class PlugTest {
//
//
//    class TestableDeviceController(mockContext: Context) : DeviceController(mockContext) {
//        // Create mocks that we can verify against
//        val mockClient: OkHttpClient = Mockito.mock(OkHttpClient::class.java)
//        val mockNotifier: DeviceNotificationManager = Mockito.mock(DeviceNotificationManager::class.java)
//
//        // Override the client and notifier properties - these must be "open" in DeviceController
//        override val client: OkHttpClient
//            get() = mockClient
//
//        override val deviceNotifier: DeviceNotificationManager
//            get() = mockNotifier
//    }
//
//    @Test
//    fun testTurnOnPlug_sendsOnUrl() {
//
//        val mockContext: Context = Mockito.mock(Context::class.java)
//
//        // If this fails, ensure your DeviceController allows overriding properties
//        val controller = TestableDeviceController(mockContext)
//
//        // Set up the mock call
//        val mockCall: Call = Mockito.mock(Call::class.java)
//
//        // Set up the behavior of the mock client
//        Mockito.`when`(controller.mockClient.newCall(any(Request::class.java))).thenReturn(mockCall)
//
//        // Act
//        controller.turnOnPlug()
//
//        // Verify the correct URL was requested
//        Mockito.verify(controller.mockClient).newCall(Mockito.argThat { req ->
//            req.url.toString() == "http://192.168.33.1/relay/0?turn=on"
//        })
//
//        // Verify enqueue was called
//        Mockito.verify(mockCall).enqueue(any(Callback::class.java))
//
//        // Verify notification was sent with the correct parameters
//        Mockito.verify(controller.mockNotifier).sendDeviceNotification(
//            deviceType = DeviceType.PLUG,
//            eventType = EventType.STATUS_CHANGE,
//            deviceName = "Shelly Plug",
//            additionalDetails = "Plug turned ON"
//        )
//    }
//
//    @Test
//    fun testTurnOffPlug_sendsOffUrl() {
//        // Create a mock context
//        val mockContext: Context = Mockito.mock(Context::class.java)
//        val controller = TestableDeviceController(mockContext)
//
//        // Set up the mock call
//        val mockCall: Call = Mockito.mock(Call::class.java)
//
//        // Set up the behavior of the mock client
//        Mockito.`when`(controller.mockClient.newCall(any(Request::class.java))).thenReturn(mockCall)
//
//        // Act
//        controller.turnOffPlug()
//
//        // Verify the correct URL was requested
//        Mockito.verify(controller.mockClient).newCall(Mockito.argThat { req ->
//            req.url.toString() == "http://192.168.33.1/relay/0?turn=off"
//        })
//
//        // Verify enqueue was called
//        Mockito.verify(mockCall).enqueue(any(Callback::class.java))
//
//        // Verify notification was sent with the correct parameters
//        Mockito.verify(controller.mockNotifier).sendDeviceNotification(
//            deviceType = DeviceType.PLUG,
//            eventType = EventType.STATUS_CHANGE,
//            deviceName = "Shelly Plug",
//            additionalDetails = "Plug turned OFF"
//        )
//    }
//}