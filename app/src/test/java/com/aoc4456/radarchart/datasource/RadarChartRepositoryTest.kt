package com.aoc4456.radarchart.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aoc4456.radarchart.MainCoroutineRule
import com.aoc4456.radarchart.datasource.database.ChartGroup
import com.aoc4456.radarchart.datasource.database.MyChart
import com.aoc4456.radarchart.datasource.database.OrderBy
import com.aoc4456.radarchart.datasource.database.SortIndex
import com.aoc4456.radarchart.datasource.fake.FakeDao
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

    private val group1 = ChartGroup(title = "Group1", color = 1234, maximumValue = 100)
    private val group2 = ChartGroup(title = "Group2", color = 5678, maximumValue = 200)
    private val groupLabelTextList = mutableListOf("Label1", "Label2", "Label3", "Label4", "Label5")
    private val chart1InGroup1 = MyChart(chartGroupId = group1.id, title = "Chart1", color = 12)
    private val chart2InGroup1 = MyChart(chartGroupId = group1.id, title = "Chart2", color = 34)
    private val chart3InGroup2 = MyChart(chartGroupId = group2.id, title = "Chart3", color = 56)
    private val chart4InGroup2 = MyChart(chartGroupId = group2.id, title = "Chart4", color = 78)
    private val chartValueIntList = mutableListOf(30, 40, 50, 60, 70)

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

    /**
     * Create
     */

    @Test
    fun saveGroupAndGetById() = mainCoroutineRule.runBlockingTest {
        // GIVEN
        repository.saveGroup(group1, groupLabelTextList, null)

        // WHEN
        val loaded = repository.getGroupById(group1.id)

        // THEN
        assertThat(loaded, notNullValue())
        assertThat(loaded.group.id, `is`(group1.id))
        assertThat(loaded.labelList.size, `is`(5))
    }

    @Test
    fun saveGroupAndGetAllGroup() = mainCoroutineRule.runBlockingTest {
        // GIVEN
        repository.saveGroup(group1, groupLabelTextList, null)
        repository.saveGroup(group2, groupLabelTextList, null)

        // WHEN
        val loaded = repository.getGroupList()

        // THEN
        assertThat(loaded.size, `is`(2))
    }

    @Test
    fun saveChartAndGetSortedChartList() = mainCoroutineRule.runBlockingTest {
        // GIVEN
        repository.saveGroup(group1, groupLabelTextList, null)
        repository.saveChart(chart1InGroup1, chartValueIntList)
        repository.saveChart(chart2InGroup1, chartValueIntList)

        // WHEN
        val loaded = repository.getSortedChartList(
            groupId = group1.id,
            sortIndex = SortIndex.CHART_TITLE,
            orderBy = OrderBy.DESC
        )

        // THEN
        assertThat(loaded.size, `is`(2))
        assertThat(loaded[0].myChart.title, `is`("Chart2"))
        assertThat(loaded[1].myChart.title, `is`("Chart1"))
    }

    @Test
    fun incrementGroupRateWhenAddGroup() = mainCoroutineRule.runBlockingTest {
        // GIVEN
        repository.saveGroup(group1, groupLabelTextList, null)
        repository.saveGroup(group2, groupLabelTextList, null)

        // WHEN
        val loadedGroup1 = repository.getGroupById(group1.id)
        val loadedGroup2 = repository.getGroupById(group2.id)

        // THEN
        assertThat(loadedGroup1.group.rate, `is`(0))
        assertThat(loadedGroup2.group.rate, `is`(1))
    }

    /**
     * Update
     */

    @Test
    fun changeGroupAscDesc() = mainCoroutineRule.runBlockingTest {
        // GIVEN
        repository.saveGroup(group1.apply { this.orderBy = OrderBy.ASC }, groupLabelTextList, null)
        repository.changeAscDesc(group1.id, OrderBy.DESC)

        // WHEN
        val loaded = repository.getGroupById(group1.id)

        // THEN
        assertThat(loaded.group.orderBy, `is`(OrderBy.DESC))
    }
}
