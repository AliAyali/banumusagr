package com.aliayali.banumusagr.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_reports")
data class WorkReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reportType: String,
    val reportDate: String,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val reportText: String,
    val problems: String,
    val tomorrowPlan: String,
    val workDurationMinutes: Int
)