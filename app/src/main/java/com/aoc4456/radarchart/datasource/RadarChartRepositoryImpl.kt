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

    override suspend fun saveGroup(
        group: ChartGroup,
        labels: List<String>,
        oldGroup: GroupWithLabelAndCharts?
    ) {
        val chartLabels = mutableListOf<ChartGroupLabel>()
        for (i in labels.indices) {
            chartLabels.add(
                ChartGroupLabel(
                    chartGroupId = group.id,
                    index = i,
                    text = labels[i]
                )
            )
        }

        withContext(ioDispatcher) {
            if (oldGroup == null) {
                radarChartDao.saveGroupAndLabel(group, chartLabels)
            } else {
                radarChartDao.updateGroupAndLabel(group, chartLabels, oldGroup)
            }
        }
    }

    override suspend fun saveChart(chart: MyChart, values: List<Int>) {
        val chartValueList = mutableListOf<ChartValue>()
        for (i in values.indices) {
            chartValueList.add(
                ChartValue(
                    myChartId = chart.id,
                    index = i,
                    value = values[i].toDouble()
                )
            )
        }

        withContext(ioDispatcher) {
            radarChartDao.saveChartAndValues(chart, chartValueList)
        }
    }

    /**
     * Read
     */

    override fun observeGroupList(): LiveData<List<GroupWithLabelAndCharts>> {
        return radarChartDao.observeGroupWithLabelAndCharts()
    }

    override suspend fun getChartList(groupId: String): List<MyChartWithValue> {
        return radarChartDao.getChartList(groupId)
    }

    /**
     * Delete
     */

    override suspend fun deleteGroup(groupId: String) {
        withContext(ioDispatcher) {
            radarChartDao.deleteGroup(groupId)
        }
    }

    override suspend fun deleteMyChart(chartId: String) {
        withContext(ioDispatcher) {
            radarChartDao.deleteMyChart(chartId)
        }
    }
}
