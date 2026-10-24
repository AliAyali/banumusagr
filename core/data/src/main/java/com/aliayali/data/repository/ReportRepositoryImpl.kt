package com.aliayali.data.repository

import com.aliayali.domain.repository.ReportRepository
import com.aliayali.model.ReportRequest
import com.aliayali.model.SendReportResult
import com.aliayali.network.ReportNetworkDataSource
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val networkDataSource: ReportNetworkDataSource,
    @param:Named("report_recipient") private val recipient: String
) : ReportRepository {

    override suspend fun sendReport(
        report: ReportRequest
    ): SendReportResult {
        val message = buildReportMessage(report)

        return try {
            val response = networkDataSource.sendReport(
                to = recipient,
                text = message
            )

            SendReportResult(
                isSuccess = response.ok,
                message = response.message
            )
        } catch (exception: Exception) {
            SendReportResult(
                isSuccess = false,
                message = exception.message
                    ?: "ارسال گزارش با خطا مواجه شد."
            )
        }
    }

    private fun buildReportMessage(report: ReportRequest): String {
        val totalHoursSection = report.totalWorkHours
            ?.takeIf { it.isNotBlank() }
            ?.let {
                """

                📊 مجموع ساعات کاری ثبت‌شده:
                $it
                """.trimIndent()
            }.orEmpty()

        return """
            📋 گزارش ${report.reportType}

            👤 نام و نام خانوادگی:
            ${report.employeeName}

            🏢 واحد:
            ${report.department}

            📅 تاریخ:
            ${report.date}

            📝 گزارش فعالیت:
            ${report.reportText}

            ⚠️ مشکلات و موانع:
            ${report.problems.ifBlank { "موردی ثبت نشده است" }}

            📌 برنامه کاری بعدی:
            ${report.tomorrowPlan.ifBlank { "موردی ثبت نشده است" }}

            ⏱ ساعات کاری امروز:
            ${report.todayWorkHours}
            $totalHoursSection
        """.trimIndent()
    }
}