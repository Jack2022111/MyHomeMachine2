package com.example.myhomemachine

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

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
