package com.example.mobilee_posyandu.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    // Default: Android Studio Emulator (10.0.2.2 mengarah ke localhost mesin host)
    // Jika menggunakan HP fisik via Wi-Fi, ganti dengan IP laptop Anda (misal: "http://192.168.1.5:8000/api/")
    var baseUrl: String = "http://10.0.2.2:8000/api/"
        private set

    private var apiServiceInstance: PosyanduApiService? = null

    fun setCustomBaseUrl(url: String) {
        var cleanUrl = url.trim()
        if (!cleanUrl.endsWith("/")) {
            cleanUrl += "/"
        }
        baseUrl = cleanUrl
        apiServiceInstance = null // Invalidate cache so client rebuilds
    }

    fun getApiService(): PosyanduApiService {
        return apiServiceInstance ?: synchronized(this) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            retrofit.create(PosyanduApiService::class.java).also {
                apiServiceInstance = it
            }
        }
    }
}

