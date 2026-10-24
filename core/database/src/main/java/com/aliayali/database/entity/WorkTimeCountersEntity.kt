package com.aliayali.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_time_counters")
data class WorkTimeCountersEntity(
    @PrimaryKey
    val id: Int = 1,

    val dailyMinutes: Int = 0,
    val weeklyMinutes: Int = 0,
    val monthlyMinutes: Int = 0,
    val totalMinutes: Int = 0
)