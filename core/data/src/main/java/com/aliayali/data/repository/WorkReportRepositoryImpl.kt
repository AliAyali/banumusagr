package com.aliayali.data.repository

import androidx.room.withTransaction
import com.aliayali.database.ReportDatabase
import com.aliayali.database.dao.ReportPeriodStateDao
import com.aliayali.database.dao.WorkReportDao
import com.aliayali.database.dao.WorkTimeCountersDao
import com.aliayali.database.entity.ReportPeriodStateEntity
import com.aliayali.database.entity.WorkReportEntity
import com.aliayali.database.entity.WorkTimeCountersEntity
import com.aliayali.domain.repository.WorkReportRepository
import com.aliayali.model.DashboardWorkStats
import com.aliayali.model.WorkReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkReportRepositoryImpl @Inject constructor(
    private val database: ReportDatabase,
    private val workReportDao: WorkReportDao,
    private val periodStateDao: ReportPeriodStateDao,
    private val countersDao: WorkTimeCountersDao
) : WorkReportRepository {
    override suspend fun markReportAsSent(id: Long) {
        workReportDao.markReportAsSent(
            reportId = id,
            sentAtMillis = System.currentTimeMillis()
        )
    }

    override fun getAllReports(): Flow<List<WorkReport>> =
        workReportDao.observeAllReports().map { reports ->
            reports.map(WorkReportEntity::asModel)
        }

    override fun getReportById(id: Long): Flow<WorkReport?> =
        workReportDao.observeReportById(id).map { it?.asModel() }

    override fun getReportsByType(type: String): Flow<List<WorkReport>> =
        workReportDao.observeReportsByType(type).map { reports ->
            reports.map(WorkReportEntity::asModel)
        }

    override fun getReportsBetween(
        startDate: String,
        endDate: String
    ): Flow<List<WorkReport>> =
        workReportDao.observeReportsBetween(startDate, endDate).map { reports ->
            reports.map(WorkReportEntity::asModel)
        }

    override fun getDailyWorkMinutes(date: String): Flow<Int> =
        workReportDao.observeDailyWorkMinutes(date)

    override fun getWorkMinutesBetween(
        startDate: String,
        endDate: String
    ): Flow<Int> =
        workReportDao.observeWorkMinutesBetween(startDate, endDate)

    override fun getTotalWorkMinutes(): Flow<Int> =
        workReportDao.observeTotalDailyWorkMinutes()

    override fun getReportCount(): Flow<Int> =
        workReportDao.observeReportCount()

    override fun getReportCountBetween(
        startDate: String,
        endDate: String
    ): Flow<Int> =
        workReportDao.observeReportCountBetween(startDate, endDate)


    override fun observeDashboardStats(
        today: String,
        tomorrow: String,
        weekStart: String,
        nextWeekStart: String,
        monthStart: String,
        nextMonthStart: String
    ): Flow<DashboardWorkStats> =
        countersDao.observeCounters().map { counters ->
            DashboardWorkStats(
                dailyMinutes = counters?.dailyMinutes ?: 0,
                weeklyMinutes = counters?.weeklyMinutes ?: 0,
                monthlyMinutes = counters?.monthlyMinutes ?: 0,
                totalMinutes = counters?.totalMinutes ?: 0
            )
        }



    override suspend fun saveReport(
        report: WorkReport,
        durationMinutes: Int
    ): Long {
        return database.withTransaction {
            val now = System.currentTimeMillis()
            val duration = durationMinutes.coerceAtLeast(0)

            val reportToSave = report.copy(
                workDurationMinutes = duration,
                createdAtMillis = now
            )

            val savedId = workReportDao.insertReport(
                reportToSave.asEntity()
            )

            val current = countersDao.getCounters()
                ?: WorkTimeCountersEntity()

            when (report.reportType) {
                REPORT_TYPE_DAILY -> {
                    countersDao.saveCounters(
                        current.copy(
                            dailyMinutes = duration,
                            weeklyMinutes = current.weeklyMinutes + duration,
                            monthlyMinutes = current.monthlyMinutes + duration,
                            totalMinutes = current.totalMinutes + duration
                        )
                    )
                }

                REPORT_TYPE_WEEKLY -> {
                    periodStateDao.save(
                        ReportPeriodStateEntity(
                            periodType = PERIOD_WEEKLY,
                            resetAtMillis = now,
                            previousMinutes = current.weeklyMinutes,
                            previousWeeklyMinutes = current.weeklyMinutes,
                            previousMonthlyMinutes = current.monthlyMinutes,
                            closedReportId = savedId,
                            closedReportDurationMinutes = duration
                        )
                    )

                    countersDao.saveCounters(
                        current.copy(weeklyMinutes = 0)
                    )
                }

                REPORT_TYPE_MONTHLY -> {
                    periodStateDao.save(
                        ReportPeriodStateEntity(
                            periodType = PERIOD_WEEKLY,
                            resetAtMillis = now,
                            previousMinutes = current.weeklyMinutes,
                            previousWeeklyMinutes = current.weeklyMinutes,
                            previousMonthlyMinutes = current.monthlyMinutes,
                            closedReportId = savedId,
                            closedReportDurationMinutes = duration
                        )
                    )

                    periodStateDao.save(
                        ReportPeriodStateEntity(
                            periodType = PERIOD_MONTHLY,
                            resetAtMillis = now,
                            previousMinutes = current.monthlyMinutes,
                            previousWeeklyMinutes = current.weeklyMinutes,
                            previousMonthlyMinutes = current.monthlyMinutes,
                            closedReportId = savedId,
                            closedReportDurationMinutes = duration
                        )
                    )

                    countersDao.saveCounters(
                        current.copy(
                            weeklyMinutes = 0,
                            monthlyMinutes = 0
                        )
                    )
                }
            }

            savedId
        }
    }



    override suspend fun updateReport(report: WorkReport) {
        require(report.id > 0L) {
            "برای ویرایش، شناسه گزارش معتبر نیست."
        }

        database.withTransaction {
            val oldReport = workReportDao
                .observeReportById(report.id)
                .first()
                ?.asModel()
                ?: throw IllegalStateException(
                    "گزارش موردنظر برای ویرایش پیدا نشد."
                )

            require(oldReport.reportType == report.reportType) {
                "نوع گزارش هنگام ویرایش نباید تغییر کند."
            }

            val newDuration = report.workDurationMinutes.coerceAtLeast(0)
            val oldDuration = oldReport.workDurationMinutes.coerceAtLeast(0)
            val difference = newDuration - oldDuration

            val updatedRows = workReportDao.updateReport(
                report.copy(
                    workDurationMinutes = newDuration
                ).asEntity()
            )

            check(updatedRows > 0) {
                "گزارش در دیتابیس به‌روزرسانی نشد."
            }

            if (difference == 0) return@withTransaction

            val counters = countersDao.getCounters()
                ?: WorkTimeCountersEntity()

            when (oldReport.reportType) {
                REPORT_TYPE_DAILY -> {
                    countersDao.saveCounters(
                        counters.copy(
                            dailyMinutes = (
                                    counters.dailyMinutes + difference
                                    ).coerceAtLeast(0),
                            weeklyMinutes = (
                                    counters.weeklyMinutes + difference
                                    ).coerceAtLeast(0),
                            monthlyMinutes = (
                                    counters.monthlyMinutes + difference
                                    ).coerceAtLeast(0),
                            totalMinutes = (
                                    counters.totalMinutes + difference
                                    ).coerceAtLeast(0)
                        )
                    )
                }

                REPORT_TYPE_WEEKLY -> {
                    val state = periodStateDao.getState(PERIOD_WEEKLY)

                    if (state?.closedReportId == report.id) {
                        countersDao.saveCounters(
                            counters.copy(
                                weeklyMinutes = (
                                        state.previousMinutes +
                                                newDuration -
                                                state.closedReportDurationMinutes
                                        ).coerceAtLeast(0)
                            )
                        )
                    }
                }

                REPORT_TYPE_MONTHLY -> {
                    val state = periodStateDao.getState(PERIOD_MONTHLY)

                    if (state?.closedReportId == report.id) {
                        countersDao.saveCounters(
                            counters.copy(
                                weeklyMinutes = state.previousWeeklyMinutes,
                                monthlyMinutes = (
                                        state.previousMinutes +
                                                newDuration -
                                                state.closedReportDurationMinutes
                                        ).coerceAtLeast(0)
                            )
                        )
                    }
                }
            }
        }
    }




    override suspend fun deleteReport(id: Long): Int {
        return database.withTransaction {
            val report = workReportDao
                .observeReportById(id)
                .first()
                ?.asModel()
                ?: return@withTransaction 0

            val deletedRows = workReportDao.deleteReport(id)

            if (deletedRows <= 0) {
                return@withTransaction deletedRows
            }

            val counters = countersDao.getCounters()
                ?: WorkTimeCountersEntity()

            when (report.reportType) {
                REPORT_TYPE_DAILY -> {
                    val duration = report.workDurationMinutes.coerceAtLeast(0)

                    countersDao.saveCounters(
                        counters.copy(
                            dailyMinutes = (counters.dailyMinutes - duration)
                                .coerceAtLeast(0),
                            weeklyMinutes = (counters.weeklyMinutes - duration)
                                .coerceAtLeast(0),
                            monthlyMinutes = (counters.monthlyMinutes - duration)
                                .coerceAtLeast(0),
                            totalMinutes = (counters.totalMinutes - duration)
                                .coerceAtLeast(0)
                        )
                    )
                }

                REPORT_TYPE_WEEKLY -> {
                    val state = periodStateDao.getState(PERIOD_WEEKLY)

                    if (state?.closedReportId == id) {
                        countersDao.saveCounters(
                            counters.copy(
                                weeklyMinutes = state.previousMinutes
                            )
                        )
                        periodStateDao.deleteState(PERIOD_WEEKLY)
                    }
                }

                REPORT_TYPE_MONTHLY -> {
                    val state = periodStateDao.getState(PERIOD_MONTHLY)

                    if (state?.closedReportId == id) {
                        countersDao.saveCounters(
                            counters.copy(
                                weeklyMinutes = state.previousWeeklyMinutes,
                                monthlyMinutes = state.previousMinutes
                            )
                        )

                        periodStateDao.deleteState(PERIOD_WEEKLY)
                        periodStateDao.deleteState(PERIOD_MONTHLY)
                    }
                }
            }

            deletedRows
        }
    }


    private fun calculateDashboardStats(
        reports: List<WorkReport>,
        today: String,
        tomorrow: String,
        weekStart: String,
        nextWeekStart: String,
        monthStart: String,
        nextMonthStart: String
    ): DashboardWorkStats {

        // از هر تاریخ فقط آخرین گزارش روزانه محاسبه می‌شود.
        val latestDailyReports = reports
            .asSequence()
            .filter { it.reportType == REPORT_TYPE_DAILY }
            .groupBy { it.reportDate }
            .mapNotNull { (_, sameDayReports) ->
                sameDayReports.maxWithOrNull(
                    compareBy<WorkReport> { it.createdAtMillis }
                        .thenBy { it.id }
                )
            }
            .toList()

        // مجموع گزارش‌های روزانه در یک بازه تاریخ.
        fun minutesBetween(
            start: String,
            end: String
        ): Int {
            return latestDailyReports
                .asSequence()
                .filter {
                    it.reportDate >= start &&
                            it.reportDate < end
                }
                .sumOf {
                    it.workDurationMinutes.coerceAtLeast(0)
                }
        }

        val todayMinutes = minutesBetween(
            start = today,
            end = tomorrow
        )

        val weeklyMinutes = minutesBetween(
            start = weekStart,
            end = nextWeekStart
        )

        val monthlyMinutes = minutesBetween(
            start = monthStart,
            end = nextMonthStart
        )

        // ساعت کل: فقط آخرین گزارش روزانه هر تاریخ در کل سابقه.
        val totalMinutes = latestDailyReports.sumOf {
            it.workDurationMinutes.coerceAtLeast(0)
        }

        return DashboardWorkStats(
            dailyMinutes = todayMinutes,
            weeklyMinutes = weeklyMinutes,
            monthlyMinutes = monthlyMinutes,
            totalMinutes = totalMinutes
        )
    }

    private companion object {
        const val REPORT_TYPE_DAILY = "روزانه"
        const val REPORT_TYPE_WEEKLY = "هفتگی"
        const val REPORT_TYPE_MONTHLY = "ماهانه"

        const val RESET_DAILY = "daily"
        const val PERIOD_WEEKLY = "weekly"
        const val PERIOD_MONTHLY = "monthly"
    }
}


private fun WorkReportEntity.asModel(): WorkReport =
    WorkReport(
        id = id,
        reportType = reportType,
        reportDate = reportDate,
        createdAtMillis = createdAtMillis,
        reportText = reportText,
        problems = problems,
        tomorrowPlan = tomorrowPlan,
        workDurationMinutes = workDurationMinutes,
        entryTime = entryTime,
        exitTime = exitTime,
        isSent = isSent,
        sentAtMillis = sentAtMillis,
        isEdited = isEdited
    )

private fun WorkReport.asEntity(): WorkReportEntity =
    WorkReportEntity(
        id = id,
        reportType = reportType,
        reportDate = reportDate,
        createdAtMillis = createdAtMillis,
        reportText = reportText,
        problems = problems,
        tomorrowPlan = tomorrowPlan,
        workDurationMinutes = workDurationMinutes,
        entryTime = entryTime,
        exitTime = exitTime,
        isSent = isSent,
        sentAtMillis = sentAtMillis,
        isEdited = isEdited
    )