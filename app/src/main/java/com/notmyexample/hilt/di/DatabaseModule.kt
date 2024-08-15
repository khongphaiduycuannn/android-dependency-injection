package com.notmyexample.hilt.di

import android.app.Application
import androidx.room.Room
import com.notmyexample.hilt.data.LogDao
import com.notmyexample.hilt.data.LogDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideLogDao(database: LogDatabase): LogDao {
        return database.logDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(
        application: Application
    ): LogDatabase {
        return Room.databaseBuilder(
            application, LogDatabase::class.java, "logging.db"
        ).build()
    }
}