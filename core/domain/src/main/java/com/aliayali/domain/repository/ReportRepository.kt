package com.aliayali.domain.repository

import com.aliayali.model.ReportRequest
import com.aliayali.model.SendReportResult

interface ReportRepository {

    suspend fun sendReport(
        report: ReportRequest
    ): SendReportResult
}