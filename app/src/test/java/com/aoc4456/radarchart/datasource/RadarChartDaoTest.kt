package com.aoc4456.radarchart.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aoc4456.radarchart.datasource.database.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executors

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
        ).allowMainThreadQueries()
            .setQueryExecutor(Executors.newSingleThreadExecutor()).build()
    }

    @After
    fun closeDb() = database.close()

    /**
     * Create Test
     */

    @Test
    fun insertGroupAndGetById() = runBlocking {
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
    fun saveGroupAndLabelsAndGetById() = runBlocking {
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
        assertThat(loaded.labelList.size, `is`(6))
        assertThat(loaded.chartList.isEmpty(), `is`(true))
    }

    @Test
    fun saveGroupAndChart() = runBlocking {
        // GIVEN
        val group = ChartGroup(title = "test", color = 12345, maximumValue = 200)
        val labels = mutableListOf<ChartGroupLabel>()
        for (i in 0..5) {
            labels.add(ChartGroupLabel(chartGroupId = group.id, index = i, text = "ラベル$i"))
        }
        database.radarChartDao().saveGroupAndLabel(group, labels)

        val myChart = MyChart(chartGroupId = group.id, title = "chartTitle", color = 12345)
        val chartValues = mutableListOf<ChartValue>()
        for (i in 0..5) {
            chartValues.add(ChartValue(myChartId = myChart.id, index = i, value = 60.0))
        }
        database.radarChartDao().saveChartAndValues(myChart, chartValues)

        // WHEN
        val loaded = database.radarChartDao().getGroupWithLabelAndCharts(group.id)

        // THEN
        assertThat(loaded.group.id, `is`(group.id))
        assertThat(loaded.labelList.size, `is`(6))
        assertThat(loaded.chartList.size, `is`(1))
        assertThat(loaded.chartList.first().title, `is`(myChart.title))
    }

    /**
     * Update Test
     */
    @Test
    fun updateGroup() = runBlocking {
        // GIVEN
        val group = ChartGroup(
            title = "test",
            color = 12345,
            maximumValue = 200
        )
        database.radarChartDao().insertGroup(group)

        val newGroup = ChartGroup(
            id = group.id,
            title = "update",
            color = 67890,
            maximumValue = 300
        )
        database.radarChartDao().updateGroup(newGroup)

        // WHEN
        val loaded = database.radarChartDao().getGroupById(group.id)

        // THEN
        assertThat(loaded, notNullValue())
        assertThat(loaded.id, `is`(group.id))
        assertThat(loaded.id, `is`(newGroup.id))
        assertThat(loaded.color, `is`(newGroup.color))
        assertThat(loaded.title, `is`("update"))
        assertThat(loaded.maximumValue, `is`(300))
    }

    /**
     * Delete Test
     */
    @Test
    fun deleteGroup() = runBlocking {
        // GIVEN
        val group = ChartGroup(title = "test", color = 12345, maximumValue = 200)
        val labels = mutableListOf<ChartGroupLabel>()
        for (i in 0..5) {
            labels.add(ChartGroupLabel(chartGroupId = group.id, index = i, text = "ラベル$i"))
        }
        database.radarChartDao().saveGroupAndLabel(group, labels)

        val myChart = MyChart(chartGroupId = group.id, title = "chartTitle", color = 12345)
        val chartValues = mutableListOf<ChartValue>()
        for (i in 0..5) {
            chartValues.add(ChartValue(myChartId = myChart.id, index = i, value = 60.0))
        }
        database.radarChartDao().saveChartAndValues(myChart, chartValues)
        database.radarChartDao().deleteGroup(group.id)

        // WHEN
        val loaded = database.radarChartDao().getGroupListWithDetail()
        val loadedGroup = database.radarChartDao().getGroupById(group.id)
        val loadedGroupLabel = database.radarChartDao().getGroupLabel(group.id)
        val loadedCharts = database.radarChartDao().getChartList(group.id)

        // THEN
        assertThat(loaded.isEmpty(), `is`(true))
        assertThat(loadedGroup, `is`(nullValue()))
        assertThat(loadedGroupLabel.isEmpty(), `is`(true))
        assertThat(loadedCharts.isEmpty(), `is`(true))
    }
}
