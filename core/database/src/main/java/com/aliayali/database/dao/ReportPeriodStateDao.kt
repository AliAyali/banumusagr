package com.aliayali.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aliayali.database.entity.ReportPeriodStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportPeriodStateDao {

    @Query("SELECT * FROM report_period_state")
    fun observeAll(): Flow<List<ReportPeriodStateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(state: ReportPeriodStateEntity)

    @Query(
        """
        SELECT * FROM report_period_state
        WHERE periodType = :type
        LIMIT 1
        """
    )
    suspend fun getState(type: String): ReportPeriodStateEntity?

    @Query(
        """
        SELECT resetAtMillis
        FROM report_period_state
        WHERE periodType = :type
        LIMIT 1
        """
    )
    suspend fun getResetTime(type: String): Long?

    @Query(
        """
        DELETE FROM report_period_state
        WHERE periodType = :type
        """
    )
    suspend fun deleteState(type: String)
}