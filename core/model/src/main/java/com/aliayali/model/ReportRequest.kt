package com.aliayali.model

data class ReportRequest(
    val reportType: String,
    val employeeName: String,
    val department: String,
    val date: String,
    val reportText: String,
    val problems: String,
    val tomorrowPlan: String,
    val todayWorkHours: String,
    val totalWorkHours: String? = null
)