package com.aoc4456.radarchart.datasource.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aoc4456.radarchart.MainCoroutineRule
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.RadarChartRepositoryImpl
import com.aoc4456.radarchart.datasource.database.ChartGroup
import com.aoc4456.radarchart.datasource.sharedpreferences.RadarChartPreferences
import com.aoc4456.radarchart.screen.chartcollection.CollectionType
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RadarChartRepositoryTest {

    private lateinit var repository: RadarChartRepository

    private val mockPreferences = mockk<RadarChartPreferences>() {
        every { loadAppLaunchCount() } returns 1
        every { loadCollectionType() } returns CollectionType.GRID
    }

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        repository = RadarChartRepositoryImpl(
            radarChartDao = FakeDao(),
            ioDispatcher = Dispatchers.Main,
            preferences = mockPreferences
        )
    }

    @Test
    fun saveGroupAndGetById() = mainCoroutineRule.runBlockingTest {
        // GIVEN
        val group = ChartGroup(title = "test", color = 12345, maximumValue = 200)
        val labels = mutableListOf<String>()
        for (i in 0..5) {
            labels.add("ラベル$i")
        }
        repository.saveGroup(group, labels, null)

        // WHEN
        val loaded = repository.getGroupById(group.id)

        // THEN
        assertThat(loaded, notNullValue())
        assertThat(loaded.group.id, `is`(group.id))
        assertThat(loaded.labelList.size, `is`(6))
    }
}
