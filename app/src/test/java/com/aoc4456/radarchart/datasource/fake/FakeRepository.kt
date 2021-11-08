package com.aoc4456.radarchart.datasource.fake

import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.ChartGroup
import com.aoc4456.radarchart.datasource.database.ChartGroupLabel
import com.aoc4456.radarchart.datasource.database.ChartValue
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.datasource.database.MyChart
import com.aoc4456.radarchart.datasource.database.MyChartWithValue
import com.aoc4456.radarchart.datasource.database.OrderBy
import com.aoc4456.radarchart.screen.chartcollection.CollectionType
import com.aoc4456.radarchart.util.MyChartOrder

/**
 * ViewModelのテストのためのFakeリポジトリ
 * // TODO コンストラクタでデータを受け取って、initで保存するよう変更する
 */
class FakeRepository : RadarChartRepository {

    private val fakeDao = FakeDao()

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
        if (oldGroup == null) {
            val maxRate = fakeDao.getMaxRate()
            group.rate = when (maxRate) {
                null -> 0
                else -> maxRate + 1
            }
            fakeDao.saveGroupAndLabel(group, chartLabels)
        } else {
            fakeDao.updateGroupAndLabel(group, chartLabels, oldGroup)
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
        fakeDao.saveChartAndValues(chart, chartValueList)
    }

    /**
     * Read
     */

    override suspend fun getGroupList(): List<GroupWithLabelAndCharts> {
        return fakeDao.getGroupListWithDetail()
    }

    override suspend fun getGroupById(groupId: String): GroupWithLabelAndCharts {
        return fakeDao.getGroupWithLabelAndCharts(groupId)
    }

    override suspend fun getSortedChartList(
        groupId: String,
        sortIndex: Int,
        orderBy: OrderBy
    ): List<MyChartWithValue> {
        val list = mutableListOf<MyChartWithValue>()
        val chartList = fakeDao.getChartList(groupId)
        chartList.forEach {
            list.add(
                MyChartWithValue(
                    myChart = it,
                    values = fakeDao.getChartValueById(it.id)
                )
            )
        }
        return MyChartOrder.getSortedChartList(list, sortIndex, orderBy)
    }

    /**
     * Update
     */

    override suspend fun changeAscDesc(groupId: String, orderBy: OrderBy) {
        fakeDao.changeAscDesc(groupId, orderBy)
    }

    override suspend fun updateSortIndex(groupId: String, sortIndex: Int) {
        fakeDao.updateSortIndex(groupId, sortIndex)
    }

    override suspend fun setGroupRates(list: List<ChartGroup>) {
        fakeDao.setRates(list)
    }

    override suspend fun swapGroupLabel(
        group: GroupWithLabelAndCharts,
        newList: List<ChartGroupLabel>
    ) {
        fakeDao.swapGroupLabel(group, newList)
    }

    /**
     * Delete
     */

    override suspend fun deleteGroup(groupId: String) {
        fakeDao.deleteGroup(groupId)
    }

    override suspend fun deleteMyChart(chartId: String) {
        fakeDao.deleteMyChart(chartId)
    }

    /**
     * SharedPreferences
     */

    private var saveType = CollectionType.LIST

    override fun saveCollectionType(type: CollectionType) {
        saveType = type
    }

    override fun loadCollectionType(): CollectionType {
        return saveType
    }
}
