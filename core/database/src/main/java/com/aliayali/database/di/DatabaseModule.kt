package com.aliayali.database.di

import android.content.Context
import androidx.room.Room
import com.aliayali.database.ReportDatabase
import com.aliayali.database.dao.EmployeeDao
import com.aliayali.database.dao.ReportPeriodStateDao
import com.aliayali.database.dao.WorkReportDao
import com.aliayali.database.dao.WorkTimeCountersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideReportDatabase(
        @ApplicationContext context: Context,
    ): ReportDatabase =
        Room.databaseBuilder(
            context,
            ReportDatabase::class.java,
            "banumusagr_database",
        ).build()

    @Provides
    fun provideEmployeeDao(
        database: ReportDatabase,
    ): EmployeeDao = database.employeeDao()

    @Provides
    fun provideWorkReportDao(
        database: ReportDatabase,
    ): WorkReportDao = database.workReportDao()

    @Provides
    fun provideReportPeriodStateDao(
        database: ReportDatabase
    ): ReportPeriodStateDao {
        return database.reportPeriodStateDao()
    }

    @Provides
    fun provideWorkTimeCountersDao(
        database: ReportDatabase
    ): WorkTimeCountersDao {
        return database.workTimeCountersDao()
    }
}