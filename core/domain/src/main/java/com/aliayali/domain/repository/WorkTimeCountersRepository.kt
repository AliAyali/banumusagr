package com.aliayali.domain.repository

import com.aliayali.model.DashboardWorkStats
import kotlinx.coroutines.flow.Flow

interface WorkTimeCountersRepository {

    fun observeCounters(): Flow<DashboardWorkStats>

    suspend fun addTotalMinutes(minutes: Int)

    suspend fun addMonthlyMinutes(minutes: Int)
}