package com.aliayali.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "report_period_state")
data class ReportPeriodStateEntity(
    @PrimaryKey
    val periodType: String,

    val resetAtMillis: Long,

    val previousMinutes: Int = 0,

    val previousWeeklyMinutes: Int = 0,
    val previousMonthlyMinutes: Int = 0,

    val closedReportId: Long? = null,

    val closedReportDurationMinutes: Int = 0
)
