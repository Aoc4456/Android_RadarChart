package com.aoc4456.radarchart.di

import android.content.Context
import androidx.room.Room
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.RadarChartRepositoryImpl
import com.aoc4456.radarchart.datasource.database.RadarChartDao
import com.aoc4456.radarchart.datasource.database.RadarChartDatabase
import com.aoc4456.radarchart.datasource.sharedpreferences.RadarChartPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDao(database: RadarChartDatabase): RadarChartDao {
        return database.radarChartDao()
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): RadarChartDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RadarChartDatabase::class.java,
            "RadarChart.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

@Module
@InstallIn(SingletonComponent::class)
object RadarChartRepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        radarChartDao: RadarChartDao,
        ioDispatcher: CoroutineDispatcher,
        preferences: RadarChartPreferences
    ): RadarChartRepository {
        return RadarChartRepositoryImpl(
            radarChartDao, ioDispatcher, preferences
        )
    }
}
