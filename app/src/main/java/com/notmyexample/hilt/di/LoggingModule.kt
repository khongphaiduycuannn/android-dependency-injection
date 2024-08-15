package com.notmyexample.hilt.di

import com.notmyexample.hilt.data.LoggerDataSource
import com.notmyexample.hilt.data.LoggerInMemoryDataSource
import com.notmyexample.hilt.data.LoggerLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class InMemoryDataSource

@Qualifier
annotation class LocalDataSource

@InstallIn(SingletonComponent::class)
@Module
abstract class LoggingModule {

    @InMemoryDataSource
    @Singleton
    @Binds
    abstract fun bindInMemoryLogger(
        impl: LoggerInMemoryDataSource
    ): LoggerDataSource

    @LocalDataSource
    @Singleton
    @Binds
    abstract fun bindLocalLogger(
        impl: LoggerLocalDataSource
    ): LoggerDataSource
}
