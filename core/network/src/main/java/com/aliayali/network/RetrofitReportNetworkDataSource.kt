package com.aliayali.network

import com.aliayali.network.model.ResponseModel
import com.aliayali.network.retrofit.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitReportNetworkDataSource @Inject constructor(
    private val apiService: ApiService,
) : ReportNetworkDataSource {

    override suspend fun sendReport(
        to: String,
        text: String,
    ): ResponseModel =
        apiService.sendReport(
            to = to,
            text = text,
        )
}