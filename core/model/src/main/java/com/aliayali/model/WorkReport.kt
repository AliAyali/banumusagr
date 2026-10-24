package com.aliayali.model

data class WorkReport(
    val id: Long = 0,
    val reportType: String,
    val reportDate: String,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val reportText: String,
    val problems: String,
    val tomorrowPlan: String,
    val workDurationMinutes: Int,
    val entryTime: String = "",
    val exitTime: String = "",
    val isSent: Boolean = false,
    val sentAtMillis: Long? = null,
    val isEdited: Boolean = false
)
