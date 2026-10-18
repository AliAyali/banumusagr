package com.aliayali.banumusagr.domain.model

enum class ReportType {
    DAILY,
    WEEKLY,
    MONTHLY
}

data class WorkReport(
    val id: Long = 0,
    val reportType: ReportType,
    val reportDate: String,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val reportText: String,
    val problems: String,
    val tomorrowPlan: String,
    val workDurationMinutes: Int
)