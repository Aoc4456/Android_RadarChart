package com.aoc4456.radarchart.di

import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.RadarChartRepositoryImpl
import com.aoc4456.radarchart.datasource.fake.FakeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * Hilt will inject a [FakeRepository] instead of a [RadarChartRepositoryImpl].
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RadarChartRepositoryModule::class]
)
abstract class TestRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindRepository(repository: FakeRepository): RadarChartRepository
}
