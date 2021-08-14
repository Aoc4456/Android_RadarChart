package com.aoc4456.radarchart.datasource

import androidx.lifecycle.LiveData
import com.aoc4456.radarchart.datasource.database.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RadarChartRepositoryImpl(
    private val radarChartDao: RadarChartDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RadarChartRepository {

    /**
     * Create
     */

    override suspend fun saveGroup(group: ChartGroup, labels: List<String>) {
        withContext(ioDispatcher) {
            radarChartDao.saveChartGroupAndLabel(group, labels)
        }
    }

    override suspend fun saveChart(chart: MyChart, values: List<Int>) {
        withContext(ioDispatcher) {
            radarChartDao.saveChartAndValues(chart, values)
        }
    }

    /**
     * Read
     */

    override fun observeGroupList(): LiveData<List<GroupWithLabelAndCharts>> {
        return radarChartDao.observeChartGroupForGroupList()
    }

    override suspend fun getChartList(groupId: String): List<MyChartWithValue> {
        return radarChartDao.getChartList(groupId)
    }

    /**
     * Delete
     */

    override suspend fun deleteGroup(groupId: String) {
        withContext(ioDispatcher) {
            radarChartDao.deleteChartGroup(groupId)
        }
    }
}
