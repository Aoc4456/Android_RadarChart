package com.aoc4456.radarchart.datasource

import androidx.lifecycle.LiveData
import com.aoc4456.radarchart.datasource.database.ChartGroup
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.datasource.database.MyChart
import com.aoc4456.radarchart.datasource.database.MyChartWithValue

interface RadarChartRepository {

    /**
     * Create
     */

    suspend fun saveGroup(group: ChartGroup, labels: List<String>, oldGroup: GroupWithLabelAndCharts?)

    suspend fun saveChart(chart: MyChart, values: List<Int>)

    /**
     * Read
     */

    fun observeGroupList(): LiveData<List<GroupWithLabelAndCharts>>

    suspend fun getChartList(groupId: String): List<MyChartWithValue>

    /**
     * Delete
     */

    suspend fun deleteGroup(groupId: String)

    suspend fun deleteMyChart(chartId: String)
}
