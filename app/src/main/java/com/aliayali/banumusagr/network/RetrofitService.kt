package com.aliayali.banumusagr.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://notificator.ir/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}