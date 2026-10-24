package com.aliayali.history.model

data class HistoryUiModel(
    val id: Long,
    val reportType: String,
    val reportDate: String,
    val reportText: String,
    val problems: String = "",
    val tomorrowPlan: String = "",
    val workDurationMinutes: Int,
    val isSent: Boolean = false,
    val createdAtMillis: Long
)

enum class HistoryFilter(val title: String) {
    ALL("همه"),
    DAILY("روزانه"),
    WEEKLY("هفتگی"),
    MONTHLY("ماهانه")
}