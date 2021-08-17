package com.aoc4456.radarchart.datasource

import androidx.lifecycle.LiveData
import com.aoc4456.radarchart.datasource.database.*

interface RadarChartRepository {

    /**
     * Create
     */

    suspend fun saveGroup(
        group: ChartGroup,
        labels: List<String>,
        oldGroup: GroupWithLabelAndCharts?
    )

    suspend fun saveChart(chart: MyChart, values: List<Int>)

    /**
     * Read
     */

    fun observeGroupList(): LiveData<List<GroupWithLabelAndCharts>>

    suspend fun getSortedChartList(
        groupId: String,
        sortIndex: Int,
        orderBy: OrderBy
    ): List<MyChartWithValue>

    /**
     * Update
     */
    suspend fun changeAscDesc(groupId: String, orderBy: OrderBy)

    /**
     * Delete
     */

    suspend fun deleteGroup(groupId: String)

    suspend fun deleteMyChart(chartId: String)
}
