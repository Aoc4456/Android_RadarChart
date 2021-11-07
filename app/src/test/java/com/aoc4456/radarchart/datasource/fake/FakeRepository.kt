package com.aoc4456.radarchart.datasource.fake

import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.*
import com.aoc4456.radarchart.screen.chartcollection.CollectionType

class FakeRepository : RadarChartRepository {
    override suspend fun saveGroup(
        group: ChartGroup,
        labels: List<String>,
        oldGroup: GroupWithLabelAndCharts?
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun saveChart(chart: MyChart, values: List<Int>) {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupList(): List<GroupWithLabelAndCharts> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupById(groupId: String): GroupWithLabelAndCharts {
        TODO("Not yet implemented")
    }

    override suspend fun getSortedChartList(
        groupId: String,
        sortIndex: Int,
        orderBy: OrderBy
    ): List<MyChartWithValue> {
        TODO("Not yet implemented")
    }

    override suspend fun changeAscDesc(groupId: String, orderBy: OrderBy) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSortIndex(groupId: String, sortIndex: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun setGroupRates(list: List<ChartGroup>) {
        TODO("Not yet implemented")
    }

    override suspend fun swapGroupLabel(
        group: GroupWithLabelAndCharts,
        newList: List<ChartGroupLabel>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGroup(groupId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMyChart(chartId: String) {
        TODO("Not yet implemented")
    }

    override fun saveCollectionType(type: CollectionType) {
        TODO("Not yet implemented")
    }

    override fun loadCollectionType(): CollectionType {
        TODO("Not yet implemented")
    }
}
