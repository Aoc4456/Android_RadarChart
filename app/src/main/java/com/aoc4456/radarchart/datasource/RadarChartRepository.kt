package com.aoc4456.radarchart.datasource

import androidx.lifecycle.LiveData
import com.aoc4456.radarchart.datasource.database.ChartGroup
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts

interface RadarChartRepository {

    suspend fun saveGroup(group: ChartGroup, labels: List<String>)

    fun observeChartGroupList(): LiveData<List<GroupWithLabelAndCharts>>
}
