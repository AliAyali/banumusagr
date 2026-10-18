package com.aliayali.banumusagr.database.repository

import com.aliayali.banumusagr.database.WorkReportDao
import com.aliayali.banumusagr.database.WorkReportEntity
import com.aliayali.banumusagr.domain.WorkReportRepository
import com.aliayali.banumusagr.domain.model.ReportType
import com.aliayali.banumusagr.domain.model.WorkReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkReportRepositoryImpl(
    private val workReportDao: WorkReportDao
) : WorkReportRepository {

    override suspend fun insertReport(report: WorkReport): Long {
        return workReportDao.insertReport(report.toEntity())
    }

    override fun observeAllReports(): Flow<List<WorkReport>> {
        return workReportDao.observeAllReports().map { reports ->
            reports.map { it.toDomain() }
        }
    }

    override fun observeReportsByType(
        type: ReportType
    ): Flow<List<WorkReport>> {
        return workReportDao.observeReportsByType(type.name)
            .map { reports ->
                reports.map { it.toDomain() }
            }
    }

    override fun observeTotalWorkMinutes(): Flow<Int> {
        return workReportDao.observeTotalWorkMinutes()
    }

    override fun observeReportCount(): Flow<Int> {
        return workReportDao.observeReportCount()
    }

    override suspend fun deleteReport(reportId: Long) {
        workReportDao.deleteReport(reportId)
    }

    private fun WorkReportEntity.toDomain() = WorkReport(
        id = id,
        reportType = ReportType.valueOf(reportType),
        reportDate = reportDate,
        createdAtMillis = createdAtMillis,
        reportText = reportText,
        problems = problems,
        tomorrowPlan = tomorrowPlan,
        workDurationMinutes = workDurationMinutes
    )

    private fun WorkReport.toEntity() = WorkReportEntity(
        id = id,
        reportType = reportType.name,
        reportDate = reportDate,
        createdAtMillis = createdAtMillis,
        reportText = reportText,
        problems = problems,
        tomorrowPlan = tomorrowPlan,
        workDurationMinutes = workDurationMinutes
    )
}