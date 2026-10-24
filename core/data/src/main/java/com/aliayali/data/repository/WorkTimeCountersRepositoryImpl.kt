package com.aliayali.data.repository

import com.aliayali.database.dao.WorkTimeCountersDao
import com.aliayali.domain.repository.WorkTimeCountersRepository
import com.aliayali.model.DashboardWorkStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkTimeCountersRepositoryImpl @Inject constructor(
    private val countersDao: WorkTimeCountersDao
) : WorkTimeCountersRepository {

    override fun observeCounters(): Flow<DashboardWorkStats> {
        return countersDao.observeCounters().map { counters ->
            DashboardWorkStats(
                dailyMinutes = counters?.dailyMinutes ?: 0,
                weeklyMinutes = counters?.weeklyMinutes ?: 0,
                monthlyMinutes = counters?.monthlyMinutes ?: 0,
                totalMinutes = counters?.totalMinutes ?: 0
            )
        }
    }

    override suspend fun addTotalMinutes(minutes: Int) {
        require(minutes >= 0) {
            "دقیقه‌ها نمی‌توانند منفی باشند."
        }

        countersDao.ensureCountersExist()

        val updatedRows = countersDao.addTotalMinutes(minutes)

        check(updatedRows > 0) {
            "به‌روزرسانی مجموع کل ساعات کاری انجام نشد."
        }
    }

    override suspend fun addMonthlyMinutes(minutes: Int) {
        require(minutes >= 0) {
            "دقیقه‌ها نمی‌توانند منفی باشند."
        }

        countersDao.ensureCountersExist()

        val updatedRows = countersDao.addMonthlyMinutes(minutes)

        check(updatedRows > 0) {
            "به‌روزرسانی ساعات ماه جاری انجام نشد."
        }
    }
}

