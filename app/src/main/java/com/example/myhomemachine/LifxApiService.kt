package com.example.myhomemachine

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

// Define the LightState data class
data class LightState(
    val power: String,        // "on" or "off"
    val brightness: Float? = null, // Optional brightness (0.0 - 1.0)
    val color: String? = null
)

interface LifxApiService {
    @PUT("v1/lights/{selector}/state")
    suspend fun setLightState(
        @Path("selector") selector: String,
        @Header("Authorization") authHeader: String,
        @Body body: @JvmSuppressWildcards LightState // Suppresses wildcard issues
    )
}

object Retrofit_Client {
    private const val BASE_URL = "https://api.lifx.com/"

    val instance: LifxApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LifxApiService::class.java)
    }
}