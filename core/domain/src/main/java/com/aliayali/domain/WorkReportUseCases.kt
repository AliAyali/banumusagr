package com.aliayali.domain

import com.aliayali.domain.repository.WorkReportRepository
import com.aliayali.model.DashboardWorkStats
import com.aliayali.model.WorkReport
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkReportUseCases @Inject constructor(
    private val repository: WorkReportRepository
) {

    suspend fun markReportAsSent(id: Long) {
        repository.markReportAsSent(id)
    }

    suspend fun updateReport(report: WorkReport) {
        repository.updateReport(report)
    }

    fun getAllReports(): Flow<List<WorkReport>> =
        repository.getAllReports()

    fun getReportById(id: Long): Flow<WorkReport?> =
        repository.getReportById(id)

    fun getReportsByType(type: String): Flow<List<WorkReport>> =
        repository.getReportsByType(type)

    fun getReportsBetween(
        startDate: String,
        endDate: String
    ): Flow<List<WorkReport>> =
        repository.getReportsBetween(startDate, endDate)

    fun getDailyWorkMinutes(date: String): Flow<Int> =
        repository.getDailyWorkMinutes(date)

    fun getWorkMinutesBetween(
        startDate: String,
        endDate: String
    ): Flow<Int> =
        repository.getWorkMinutesBetween(startDate, endDate)

    fun getTotalWorkMinutes(): Flow<Int> =
        repository.getTotalWorkMinutes()

    fun getReportCount(): Flow<Int> =
        repository.getReportCount()

    fun getReportCountBetween(
        startDate: String,
        endDate: String
    ): Flow<Int> =
        repository.getReportCountBetween(startDate, endDate)

    suspend fun saveReport(
        report: WorkReport,
        durationMinutes: Int = report.workDurationMinutes
    ): Long {
        return repository.saveReport(report, durationMinutes)
    }

    suspend fun deleteReport(id: Long): Int {
        return repository.deleteReport(id)
    }

    fun observeDashboardStats(
        today: String,
        tomorrow: String,
        weekStart: String,
        nextWeekStart: String,
        monthStart: String,
        nextMonthStart: String
    ): Flow<DashboardWorkStats> =
        repository.observeDashboardStats(
            today = today,
            tomorrow = tomorrow,
            weekStart = weekStart,
            nextWeekStart = nextWeekStart,
            monthStart = monthStart,
            nextMonthStart = nextMonthStart
        )
}