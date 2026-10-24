package com.aliayali.network

import com.aliayali.network.model.ResponseModel

interface ReportNetworkDataSource {

    suspend fun sendReport(
        to: String,
        text: String,
    ): ResponseModel
}