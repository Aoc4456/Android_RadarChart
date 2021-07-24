package com.aoc4456.radarchart.datasource

import com.aoc4456.radarchart.datasource.database.ChartGroup
import com.aoc4456.radarchart.datasource.database.RadarChartDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RadarChartRepositoryImpl(
    private val radarChartDao: RadarChartDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RadarChartRepository {

    override suspend fun saveGroup(group: ChartGroup, labels: List<String>) {
        withContext(ioDispatcher) {
            radarChartDao.saveChartGroupAndLabel(group, labels)
        }
    }
}
