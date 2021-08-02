package com.aoc4456.radarchart.datasource

import androidx.lifecycle.LiveData
import com.aoc4456.radarchart.datasource.database.ChartGroup
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.datasource.database.RadarChartDao
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

    /**
     * Read
     */

    override fun observeChartGroupList(): LiveData<List<GroupWithLabelAndCharts>> {
        return radarChartDao.observeChartGroupForGroupList()
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
