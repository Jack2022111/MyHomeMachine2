package com.example.myhomemachine

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class RetrofitClient {
    open val instance: LifxApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.lifx.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LifxApiService::class.java)
    }
}