package com.aoc4456.radarchart.datasource

import com.aoc4456.radarchart.datasource.database.*
import com.aoc4456.radarchart.datasource.sharedpreferences.RadarChartPreferences
import com.aoc4456.radarchart.screen.chartcollection.CollectionType
import com.aoc4456.radarchart.util.MyChartOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RadarChartRepositoryImpl(
    private val radarChartDao: RadarChartDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val preferences: RadarChartPreferences
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

    override suspend fun getGroupList(): List<GroupWithLabelAndCharts> {
        return radarChartDao.getGroupListWithDetail()
    }

    override suspend fun getGroupById(groupId: String): GroupWithLabelAndCharts {
        return radarChartDao.getGroupWithLabelAndCharts(groupId)
    }

    override suspend fun getSortedChartList(
        groupId: String,
        sortIndex: Int,
        orderBy: OrderBy
    ): List<MyChartWithValue> {
        val list = mutableListOf<MyChartWithValue>()
        val chartList = radarChartDao.getChartList(groupId)
        chartList.forEach {
            list.add(
                MyChartWithValue(
                    myChart = it,
                    values = radarChartDao.getChartValueById(it.id)
                )
            )
        }
        return MyChartOrder.getSortedChartList(list, sortIndex, orderBy)
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

    /**
     * Shared Preferences
     */

    override fun saveCollectionType(type: CollectionType) {
        preferences.saveCollectionType(type)
    }

    override fun loadCollectionType(): CollectionType {
        return preferences.loadCollectionType()
    }
}
