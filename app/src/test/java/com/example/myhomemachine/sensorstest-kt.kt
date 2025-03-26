// SensorsTest.kt
package com.example.myhomemachine

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.ArgumentMatchers.anyString
import retrofit2.Call
import retrofit2.Response
import kotlin.math.ln
import kotlin.math.exp

class SensorsTest {

    // Create a wrapper class to make the code more testable
    class SensorController {
        // Extract the fetchMeterStatus function logic from MainActivity
        suspend fun fetchMeterStatus(
            apiService: SwitchBotApiService,
            deviceId: String,
            token: String,
            secret: String
        ): MeterStatus? {
            val timestamp = System.currentTimeMillis().toString()
            val nonce = java.util.UUID.randomUUID().toString()
            val sign = generateSignature(token, secret, timestamp, nonce)
            
            try {
                val response = apiService.getDeviceStatus(deviceId, token, sign, nonce, timestamp).execute()
                
                if (response.isSuccessful) {
                    val body = response.body()?.body
                    val temp = body?.temperature
                    val hum = body?.humidity
                    val bat = body?.battery
                    
                    if (temp != null && hum != null && bat != null) {
                        val temperature = temp.toDouble()
                        val humidity = hum.toDouble()
                        
                        // Compute Dew Point using the Magnus-Tetens approximation
                        val a = 17.27
                        val b = 237.7
                        val gamma = ln(humidity / 100.0) + (a * temperature) / (b + temperature)
                        val dewPoint = (b * gamma) / (a - gamma)
                        
                        // Compute Heat Index
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
                        
                        return MeterStatus(
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
                    }
                }
                return null
            } catch (e: Exception) {
                return null
            }
        }
        
        // Helper function to compute heat index
        private fun computeHeatIndex(tempC: Double, rh: Double): Double {
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
        
        // Generate signature function copied from MainActivity
        private fun generateSignature(token: String, secret: String, timestamp: String, nonce: String): String {
            val data = token + timestamp + nonce
            val mac = javax.crypto.Mac.getInstance("HmacSHA256")
            mac.init(javax.crypto.spec.SecretKeySpec(secret.toByteArray(), "HmacSHA256"))
            return android.util.Base64.encodeToString(mac.doFinal(data.toByteArray()), android.util.Base64.NO_WRAP)
        }
    }

    @Test
    fun testFetchMeterStatus_returnsCorrectValues() = runTest {
        // Test data
        val testTemp = 22.5f
        val testHumidity = 60.0f
        val testBattery = 90.0f
        
        // Mock API service
        val mockApiService = Mockito.mock(SwitchBotApiService::class.java)
        val mockCall = Mockito.mock(Call::class.java) as Call<SwitchBotResponse>
        
        // Create test response
        val dummyDeviceStatus = DeviceStatus(
            deviceId = "F90D2BD4D12F", 
            deviceType = "Meter",
            hubDeviceId = "hub123",
            humidity = testHumidity,
            temperature = testTemp,
            battery = testBattery
        )
        val dummyResponse = SwitchBotResponse(
            statusCode = 200, 
            body = dummyDeviceStatus,
            message = "success"
        )
        
        // Mock response object
        val mockResponse = Mockito.mock(Response::class.java) as Response<SwitchBotResponse>
        Mockito.`when`(mockResponse.isSuccessful).thenReturn(true)
        Mockito.`when`(mockResponse.body()).thenReturn(dummyResponse)
        
        // Setup the mock call
        Mockito.`when`(mockCall.execute()).thenReturn(mockResponse)
        Mockito.`when`(
            mockApiService.getDeviceStatus(
                anyString(), 
                anyString(), 
                anyString(), 
                anyString(), 
                anyString()
            )
        ).thenReturn(mockCall)
        
        // Create controller and execute
        val controller = SensorController()
        val result = controller.fetchMeterStatus(
            apiService = mockApiService,
            deviceId = "testDeviceId",
            token = "testToken",
            secret = "testSecret"
        )
        
        // Verify the result
        assertNotNull("MeterStatus should not be null", result)
        
        // Check basic values
        assertEquals("Temperature should match", testTemp.toDouble(), result!!.temperature, 0.001)
        assertEquals("Humidity should match", testHumidity.toDouble(), result.humidity, 0.001)
        assertEquals("Battery should match", testBattery.toInt(), result.battery)
        
        // Calculate expected derived values
        val expectedTemp = testTemp.toDouble()
        val expectedHumidity = testHumidity.toDouble()
        
        // Expected dew point calculation
        val a = 17.27
        val b = 237.7
        val gamma = ln(expectedHumidity / 100.0) + (a * expectedTemp) / (b + expectedTemp)
        val expectedDewPoint = (b * gamma) / (a - gamma)
        
        // Expected saturation vapor pressure
        val expectedSatVaporPressure = 6.112 * exp(17.67 * expectedTemp / (expectedTemp + 243.5))
        val expectedVaporPressure = (expectedHumidity / 100.0) * expectedSatVaporPressure
        
        // Verify derived values
        assertEquals("Dew point should match", expectedDewPoint, result.dewPoint, 0.001)
        assertEquals("Saturation vapor pressure should match", expectedSatVaporPressure, result.saturationVaporPressure, 0.001)
        assertEquals("Vapor pressure should match", expectedVaporPressure, result.vaporPressure, 0.001)
    }
}