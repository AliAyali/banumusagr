package com.aliayali.home.model

data class HomeReportUiModel(
    val id: Long,
    val reportTypeTitle: String,
    val reportDate: String,
    val reportText: String,
    val problems: String,
    val tomorrowPlan: String,
    val workDurationMinutes: Int,
    val reportType: String,
    val isSent: Boolean,
    val isLatestOfType: Boolean = false
)