package com.aliayali.banumusagr.domain

import com.aliayali.banumusagr.domain.model.ReportType
import com.aliayali.banumusagr.domain.model.WorkReport
import kotlinx.coroutines.flow.Flow

interface WorkReportRepository {

    suspend fun insertReport(report: WorkReport): Long

    fun observeAllReports(): Flow<List<WorkReport>>

    fun observeReportsByType(
        type: ReportType
    ): Flow<List<WorkReport>>

    fun observeTotalWorkMinutes(): Flow<Int>

    fun observeReportCount(): Flow<Int>

    suspend fun deleteReport(reportId: Long)
}