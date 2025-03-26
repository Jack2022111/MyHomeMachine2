package com.example.myhomemachine

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

// The actual interface from your codebase
interface SwitchBotApiService {
    @GET("v1.1/devices/{deviceId}/status")
    fun getDeviceStatus(
        @Path("deviceId") deviceId: String,
        @Header("Authorization") token: String,
        @Header("sign") sign: String,
        @Header("nonce") nonce: String,
        @Header("t") timestamp: String
    ): Call<SwitchBotResponse>
}

// These response models are needed for the tests
data class SwitchBotResponse(
    val statusCode: Int,
    val body: DeviceStatus,
    val message: String
)

data class DeviceStatus(
    val deviceId: String,
    val deviceType: String,
    val hubDeviceId: String,
    val humidity: Float,
    val temperature: Float,
    val battery: Float
)

// This model is used in fetchMeterStatus to return calculated values
data class MeterStatus(
    val temperature: Double,
    val humidity: Double,
    val battery: Int,
    val dewPoint: Double,
    val heatIndex: Double,
    val absoluteHumidity: Double,
    val vaporPressure: Double,
    val saturationVaporPressure: Double,
    val vaporPressureDeficit: Double,
    val mixingRatio: Double,
    val enthalpy: Double
)