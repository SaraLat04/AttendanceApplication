package com.example.gestion_absences

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://100.70.38.250:8000"

    private var retrofit: Retrofit? = null

    @JvmStatic
    fun getClient(): Retrofit{

    if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

