package com.aliayali.domain.repository

import com.aliayali.model.DashboardWorkStats
import com.aliayali.model.WorkReport
import kotlinx.coroutines.flow.Flow

interface WorkReportRepository {
    suspend fun markReportAsSent(id: Long)

    fun getAllReports(): Flow<List<WorkReport>>

    fun getReportById(id: Long): Flow<WorkReport?>

    fun getReportsByType(type: String): Flow<List<WorkReport>>

    fun getReportsBetween(
        startDate: String,
        endDate: String
    ): Flow<List<WorkReport>>

    fun getDailyWorkMinutes(date: String): Flow<Int>

    fun getWorkMinutesBetween(
        startDate: String,
        endDate: String
    ): Flow<Int>

    fun getTotalWorkMinutes(): Flow<Int>

    fun getReportCount(): Flow<Int>

    fun getReportCountBetween(
        startDate: String,
        endDate: String
    ): Flow<Int>

    fun observeDashboardStats(
        today: String,
        tomorrow: String,
        weekStart: String,
        nextWeekStart: String,
        monthStart: String,
        nextMonthStart: String
    ): Flow<DashboardWorkStats>

    suspend fun saveReport(
        report: WorkReport,
        durationMinutes: Int = report.workDurationMinutes
    ): Long

    suspend fun updateReport(report: WorkReport)

    suspend fun deleteReport(id: Long): Int
}