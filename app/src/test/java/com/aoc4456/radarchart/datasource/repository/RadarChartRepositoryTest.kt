package com.aoc4456.radarchart.datasource.repository

import androidx.test.core.app.ApplicationProvider
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.RadarChartRepositoryImpl
import com.aoc4456.radarchart.datasource.sharedpreferences.RadarChartPreferences
import com.aoc4456.radarchart.screen.chartcollection.CollectionType
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Before

class RadarChartRepositoryTest {

    private lateinit var repository: RadarChartRepository

    private val mockPreferences = mockk<RadarChartPreferences>(){
        every { loadAppLaunchCount() } returns 1
        every { loadCollectionType() } returns CollectionType.GRID
    }

    @Before
    fun createRepository() {
        repository = RadarChartRepositoryImpl(
            radarChartDao = FakeDao(),
            ioDispatcher = Dispatchers.Main,
            preferences = mockPreferences
        )
    }
}

