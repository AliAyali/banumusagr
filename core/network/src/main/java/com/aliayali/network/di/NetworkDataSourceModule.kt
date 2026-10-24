package com.aliayali.network.di

import com.aliayali.network.ReportNetworkDataSource
import com.aliayali.network.RetrofitReportNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindReportNetworkDataSource(
        impl: RetrofitReportNetworkDataSource,
    ): ReportNetworkDataSource
}