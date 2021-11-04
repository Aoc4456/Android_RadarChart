package com.aoc4456.radarchart.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aoc4456.radarchart.datasource.database.ChartGroup
import com.aoc4456.radarchart.datasource.database.ChartGroupLabel
import com.aoc4456.radarchart.datasource.database.RadarChartDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RadarChartDaoTest {

    private lateinit var database: RadarChartDatabase

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RadarChartDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertGroupAndGetById() = runBlockingTest {
        // GIVEN - Groupをインサート
        val group = ChartGroup(
            title = "test",
            color = 12345,
            maximumValue = 200
        )
        database.radarChartDao().insertGroup(group)

        // WHEN - Groupをデータベースから取得
        val loaded = database.radarChartDao().getGroupById(group.id)

        // THEN - グループが一つだけあり、期待値が含まれること
        assertThat(loaded, notNullValue())
        assertThat(loaded.id, `is`(group.id))
        assertThat(loaded.color, `is`(group.color))
        assertThat(loaded.maximumValue, `is`(200))
    }

    @Test
    fun insertGroupAndLabelsAndGetById() = runBlockingTest {
        // GIVEN - GroupLabelをインサート
        val group = ChartGroup(title = "test", color = 12345, maximumValue = 200)
        val labels = mutableListOf<ChartGroupLabel>()
        for (i in 0..5) {
            labels.add(ChartGroupLabel(chartGroupId = group.id, index = i, text = "ラベル$i"))
        }
        database.radarChartDao().saveGroupAndLabel(group, labels)

        // WHEN GroupLabelをデータベースから取得
        val loaded = database.radarChartDao().getGroupWithLabelAndCharts(group.id)

        // THEN - 入れた分だけ取得できること
        assertThat(loaded, notNullValue())
        assertThat(loaded.group.id, `is`(group.id))
        assertThat(loaded.labelList.size, `is`(3))
        assertThat(loaded.chartList.isEmpty(), `is`(true))
    }
}
