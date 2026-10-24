package com.aliayali.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aliayali.database.entity.WorkReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkReportDao {

    // --------------------------------------------------
    // Observe report by ID
    // --------------------------------------------------

    @Query(
        """
        SELECT *
        FROM work_reports
        WHERE id = :id
        LIMIT 1
        """
    )
    fun observeReportById(
        id: Long
    ): Flow<WorkReportEntity?>

    // --------------------------------------------------
    // Insert report
    // --------------------------------------------------

    @Insert
    suspend fun insertReport(
        report: WorkReportEntity
    ): Long

    // --------------------------------------------------
    // Observe all reports
    // --------------------------------------------------

    @Query(
        """
        SELECT *
        FROM work_reports
        ORDER BY createdAtMillis DESC, id DESC
        """
    )
    fun observeAllReports(): Flow<List<WorkReportEntity>>

    // --------------------------------------------------
    // Observe reports by type
    // --------------------------------------------------

    @Query(
        """
        SELECT *
        FROM work_reports
        WHERE reportType = :type
        ORDER BY createdAtMillis DESC, id DESC
        """
    )
    fun observeReportsByType(
        type: String
    ): Flow<List<WorkReportEntity>>

    // --------------------------------------------------
    // Observe reports within a date range
    // startDate: inclusive
    // endDate: exclusive
    // --------------------------------------------------

    @Query(
        """
        SELECT *
        FROM work_reports
        WHERE reportDate >= :startDate
          AND reportDate < :endDate
        ORDER BY reportDate DESC, createdAtMillis DESC, id DESC
        """
    )
    fun observeReportsBetween(
        startDate: String,
        endDate: String
    ): Flow<List<WorkReportEntity>>

    // --------------------------------------------------
    // Observe unsent reports
    // --------------------------------------------------

    @Query(
        """
        SELECT *
        FROM work_reports
        WHERE isSent = 0
        ORDER BY createdAtMillis DESC, id DESC
        """
    )
    fun observeUnsentReports(): Flow<List<WorkReportEntity>>

    // --------------------------------------------------
    // Mark report as sent
    // --------------------------------------------------

    @Query(
        """
    UPDATE work_reports
    SET isSent = 1,
        sentAtMillis = :sentAtMillis
    WHERE id = :reportId
    """
    )
    suspend fun markReportAsSent(
        reportId: Long,
        sentAtMillis: Long = System.currentTimeMillis()
    ): Int

    // --------------------------------------------------
    // Total work minutes
    // Only the latest daily report per date is counted.
    // Weekly and monthly reports are excluded.
    // --------------------------------------------------

    @Query(
        """
        SELECT COALESCE(SUM(r.workDurationMinutes), 0)
        FROM work_reports AS r
        WHERE r.reportType = 'روزانه'
          AND r.id = (
              SELECT r2.id
              FROM work_reports AS r2
              WHERE r2.reportType = 'روزانه'
                AND r2.reportDate = r.reportDate
              ORDER BY r2.createdAtMillis DESC, r2.id DESC
              LIMIT 1
          )
        """
    )
    fun observeTotalWorkMinutes(): Flow<Int>

    // Alias used by the dashboard repository.
    // Keep this method so existing repository code remains compatible.

    @Query(
        """
        SELECT COALESCE(SUM(r.workDurationMinutes), 0)
        FROM work_reports AS r
        WHERE r.reportType = 'روزانه'
          AND r.id = (
              SELECT r2.id
              FROM work_reports AS r2
              WHERE r2.reportType = 'روزانه'
                AND r2.reportDate = r.reportDate
              ORDER BY r2.createdAtMillis DESC, r2.id DESC
              LIMIT 1
          )
        """
    )
    fun observeTotalDailyWorkMinutes(): Flow<Int>

    // --------------------------------------------------
    // Work minutes between dates
    // Only the latest daily report per date is counted.
    // --------------------------------------------------

    @Query(
        """
        SELECT COALESCE(SUM(r.workDurationMinutes), 0)
        FROM work_reports AS r
        WHERE r.reportType = 'روزانه'
          AND r.reportDate >= :startDate
          AND r.reportDate < :endDate
          AND r.id = (
              SELECT r2.id
              FROM work_reports AS r2
              WHERE r2.reportType = 'روزانه'
                AND r2.reportDate = r.reportDate
              ORDER BY r2.createdAtMillis DESC, r2.id DESC
              LIMIT 1
          )
        """
    )
    fun observeWorkMinutesBetween(
        startDate: String,
        endDate: String
    ): Flow<Int>

    // --------------------------------------------------
    // Daily work minutes
    // Only the latest daily report for this date is counted.
    // Returns 0 if no daily report exists.
    // --------------------------------------------------

    @Query(
        """
        SELECT COALESCE(
            (
                SELECT workDurationMinutes
                FROM work_reports
                WHERE reportType = 'روزانه'
                  AND reportDate = :date
                ORDER BY createdAtMillis DESC, id DESC
                LIMIT 1
            ),
            0
        )
        """
    )
    fun observeDailyWorkMinutes(
        date: String
    ): Flow<Int>

    // --------------------------------------------------
    // Daily work minutes since a reset timestamp
    // Counts only the latest daily report per date
    // that was created after the reset.
    // --------------------------------------------------

    @Query(
        """
        SELECT COALESCE(SUM(r.workDurationMinutes), 0)
        FROM work_reports AS r
        WHERE r.reportType = :dailyType
          AND r.createdAtMillis > :resetAtMillis
          AND r.id = (
              SELECT r2.id
              FROM work_reports AS r2
              WHERE r2.reportType = :dailyType
                AND r2.reportDate = r.reportDate
                AND r2.createdAtMillis > :resetAtMillis
              ORDER BY r2.createdAtMillis DESC, r2.id DESC
              LIMIT 1
          )
        """
    )
    fun observeDailyMinutesAfterReset(
        dailyType: String,
        resetAtMillis: Long
    ): Flow<Int>

    // --------------------------------------------------
    // Total report count
    // Counts every report type.
    // --------------------------------------------------

    @Query(
        """
        SELECT COUNT(*)
        FROM work_reports
        """
    )
    fun observeReportCount(): Flow<Int>

    // --------------------------------------------------
    // Report count within a date range
    // Counts every report type.
    // --------------------------------------------------

    @Query(
        """
        SELECT COUNT(*)
        FROM work_reports
        WHERE reportDate >= :startDate
          AND reportDate < :endDate
        """
    )
    fun observeReportCountBetween(
        startDate: String,
        endDate: String
    ): Flow<Int>

    // --------------------------------------------------
    // Delete report
    // --------------------------------------------------

    @Query(
        """
    DELETE FROM work_reports
    WHERE id = :reportId
    """
    )
    suspend fun deleteReport(reportId: Long): Int

    // Update existing report
    @Update
    suspend fun updateReport(
        report: WorkReportEntity
    ): Int
}