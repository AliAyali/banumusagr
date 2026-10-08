package com.aliayali.banumusagr.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("send")
    suspend fun sendReport(
        @Query("to") to: String,
        @Query("text") text: String
    ): ResponseModel
}