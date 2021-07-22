package com.aoc4456.radarchart.datasource

import com.aoc4456.radarchart.datasource.database.ChartGroup

interface RadarChartRepository {

    suspend fun saveGroup(group: ChartGroup)

}
