package com.aliayali.data.di

import com.aliayali.data.repository.EmployeeRepositoryImpl
import com.aliayali.data.repository.ReportRepositoryImpl
import com.aliayali.data.repository.WorkReportRepositoryImpl
import com.aliayali.data.repository.WorkTimeCountersRepositoryImpl
import com.aliayali.domain.repository.EmployeeRepository
import com.aliayali.domain.repository.ReportRepository
import com.aliayali.domain.repository.WorkReportRepository
import com.aliayali.domain.repository.WorkTimeCountersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEmployeeRepository(
        impl: EmployeeRepositoryImpl,
    ): EmployeeRepository

    @Binds
    @Singleton
    abstract fun bindWorkReportRepository(
        impl: WorkReportRepositoryImpl,
    ): WorkReportRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(
        impl: ReportRepositoryImpl
    ): ReportRepository

    @Binds
    @Singleton
    abstract fun bindWorkTimeCountersRepository(
        implementation: WorkTimeCountersRepositoryImpl
    ): WorkTimeCountersRepository
}
