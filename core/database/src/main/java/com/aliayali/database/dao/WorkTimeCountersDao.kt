package com.aliayali.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aliayali.database.entity.WorkTimeCountersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkTimeCountersDao {

    @Query(
        """
        SELECT * FROM work_time_counters
        WHERE id = 1
        LIMIT 1
        """
    )
    fun observeCounters(): Flow<WorkTimeCountersEntity?>

    @Query(
        """
        SELECT * FROM work_time_counters
        WHERE id = 1
        LIMIT 1
        """
    )
    suspend fun getCounters(): WorkTimeCountersEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCounters(
        counters: WorkTimeCountersEntity
    )


    @Query("""
    INSERT OR IGNORE INTO work_time_counters
    (id, dailyMinutes, weeklyMinutes, monthlyMinutes, totalMinutes)
    VALUES (1, 0, 0, 0, 0)
""")
    suspend fun ensureCountersExist()

    @Query("""
    UPDATE work_time_counters
    SET totalMinutes = totalMinutes + :minutes
    WHERE id = 1
""")
    suspend fun addTotalMinutes(minutes: Int): Int

    @Query("""
    UPDATE work_time_counters
    SET monthlyMinutes = monthlyMinutes + :minutes
    WHERE id = 1
""")
    suspend fun addMonthlyMinutes(minutes: Int): Int
}