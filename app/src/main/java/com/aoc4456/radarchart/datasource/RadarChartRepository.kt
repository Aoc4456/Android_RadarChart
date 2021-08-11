package com.aoc4456.radarchart.datasource

import androidx.lifecycle.LiveData
import com.aoc4456.radarchart.datasource.database.ChartGroup
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.datasource.database.MyChart

interface RadarChartRepository {

    /**
     * Create
     */

    suspend fun saveGroup(group: ChartGroup, labels: List<String>)

    suspend fun saveChart(chart: MyChart, values: List<Int>)

    /**
     * Read
     */

    fun observeChartGroupList(): LiveData<List<GroupWithLabelAndCharts>>

    /**
     * Delete
     */

    suspend fun deleteGroup(groupId: String)
}
