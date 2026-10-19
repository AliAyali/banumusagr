package com.aliayali.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkReportDao {

    @Insert
    suspend fun insertReport(report: WorkReportEntity): Long

    @Query(
        """
        SELECT * FROM work_reports
        ORDER BY createdAtMillis DESC, id DESC
        """
    )
    fun observeAllReports(): Flow<List<WorkReportEntity>>

    @Query(
        """
        SELECT * FROM work_reports
        WHERE reportType = :type
        ORDER BY createdAtMillis DESC, id DESC
        """
    )
    fun observeReportsByType(
        type: String,
    ): Flow<List<WorkReportEntity>>

    @Query(
        """
        SELECT COALESCE(SUM(workDurationMinutes), 0)
        FROM work_reports
        """
    )
    fun observeTotalWorkMinutes(): Flow<Int>

    @Query("SELECT COUNT(*) FROM work_reports")
    fun observeReportCount(): Flow<Int>

    @Query(
        """
        SELECT COALESCE(SUM(workDurationMinutes), 0)
        FROM work_reports
        WHERE reportDate >= :startDate
          AND reportDate <= :endDate
        """
    )
    fun observeWorkMinutesBetween(
        startDate: String,
        endDate: String,
    ): Flow<Int>

    @Query("DELETE FROM work_reports WHERE id = :reportId")
    suspend fun deleteReport(reportId: Long)
}