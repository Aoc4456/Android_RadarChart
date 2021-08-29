package com.aoc4456.radarchart.datasource

import androidx.lifecycle.LiveData
import com.aoc4456.radarchart.datasource.database.*
import com.aoc4456.radarchart.util.MyChartOrder
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
                val maxRate = radarChartDao.getMaxRate()
                group.rate = when (maxRate) {
                    null -> 0
                    else -> maxRate + 1
                }
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

    override suspend fun getGroupById(groupId: String): GroupWithLabelAndCharts {
        return radarChartDao.getGroupById(groupId)
    }

    override suspend fun getSortedChartList(
        groupId: String,
        sortIndex: Int,
        orderBy: OrderBy
    ): List<MyChartWithValue> {
        val chartList = radarChartDao.getChartList(groupId)
        return MyChartOrder.getSortedChartList(chartList, sortIndex, orderBy)
    }

    /**
     * Update
     */
    override suspend fun changeAscDesc(groupId: String, orderBy: OrderBy) {
        radarChartDao.changeAscDesc(groupId, orderBy)
    }

    override suspend fun updateSortIndex(groupId: String, sortIndex: Int) {
        radarChartDao.updateSortIndex(groupId, sortIndex)
    }

    override suspend fun setGroupRates(list: List<ChartGroup>) {
        radarChartDao.setRates(list)
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
