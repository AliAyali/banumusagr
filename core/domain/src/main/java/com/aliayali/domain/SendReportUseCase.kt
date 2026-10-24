package com.aliayali.domain

import com.aliayali.domain.repository.ReportRepository
import com.aliayali.model.ReportRequest
import com.aliayali.model.SendReportResult
import javax.inject.Inject

class SendReportUseCase @Inject constructor(
    private val repository: ReportRepository
) {
    suspend operator fun invoke(
        report: ReportRequest
    ): SendReportResult =
        repository.sendReport(report)
}