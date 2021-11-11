package com.aoc4456.radarchart.datasource.fake

import com.aoc4456.radarchart.datasource.database.*

/**
 * Repositoryをテストする際に使用する偽のDao
 *
 * データベースではなくメモリに値を保存する
 */
class FakeDao : RadarChartDao {

    private val groupList = mutableListOf<ChartGroup>()
    private val groupLabelList = mutableListOf<ChartGroupLabel>()
    private val chartList = mutableListOf<MyChart>()
    private val chartValues = mutableListOf<ChartValue>()

    /**
     * Create
     */

    override suspend fun insertGroup(group: ChartGroup) {
        groupList.add(group)
    }

    override suspend fun insertGroupLabel(label: ChartGroupLabel) {
        groupLabelList.add(label)
    }

    override suspend fun insertChart(chart: MyChart) {
        chartList.add(chart)
    }

    override suspend fun insertChartValue(chartValue: ChartValue) {
        chartValues.add(chartValue)
    }

    /**
     * Read
     */

    override suspend fun getGroupIdList(): List<String> {
        return groupList.map { it.id }
    }

    override suspend fun getGroupById(groupId: String): ChartGroup {
        return groupList.first { it.id == groupId }
    }

    override suspend fun getGroupLabel(groupId: String): List<ChartGroupLabel> {
        return groupLabelList.filter { it.chartGroupId == groupId }
    }

    override suspend fun getChartList(groupId: String): List<MyChart> {
        return chartList.filter { it.chartGroupId == groupId }
    }

    override suspend fun getChartValueById(chartId: String): List<ChartValue> {
        return chartValues.filter { it.myChartId == chartId }
    }

    override suspend fun getMaxRate(): Int? {
        val rateList = groupList.map { it.rate }
        return rateList.maxOrNull()
    }

    /**
     * Update
     */

    override suspend fun updateGroup(group: ChartGroup) {
        groupList.removeAll { it.id == group.id }
        groupList.add(group)
    }

    override suspend fun updateGroupLabel(label: ChartGroupLabel) {
        groupLabelList.removeAll { it.id == label.id }
        groupLabelList.add(label)
    }

    override suspend fun updateChartValue(value: ChartValue) {
        chartValues.removeAll { it.id == value.id }
        chartValues.add(value)
    }

    override suspend fun changeAscDesc(groupId: String, orderBy: OrderBy) {
        groupList.find { it.id == groupId }.apply {
            this?.orderBy = orderBy
        }
    }

    override suspend fun updateSortIndex(groupId: String, sortIndex: Int) {
        groupList.find { it.id == groupId }.apply {
            this?.sortIndex = sortIndex
        }
    }

    override suspend fun resetSortIndex(groupId: String) {
        groupList.find { it.id == groupId }.apply {
            this?.sortIndex = -1
        }
    }

    override suspend fun setRate(groupId: String, rate: Int) {
        groupList.find { it.id == groupId }.apply {
            this?.sortIndex = -1
        }
    }

    override suspend fun swapChartValueIndex(chartId: String, oldIndex: Int, newIndex: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun resetSortingState() {
        chartValues.forEach { it.sortingState = 0 }
    }

    /**
     * Delete
     */

    override suspend fun deleteGroup(groupId: String) {
        groupList.removeAll { it.id == groupId }
        groupLabelList.removeAll { it.chartGroupId == groupId }
        chartList.forEach { chart ->
            chartValues.removeAll { it.myChartId == chart.id }
        }
        chartList.removeAll { it.chartGroupId == groupId }
    }

    override suspend fun deleteGroupLabel(groupId: String) {
        groupLabelList.removeAll { it.chartGroupId == groupId }
    }

    override suspend fun deleteMyChart(chartId: String) {
        chartList.removeAll { it.id == chartId }
        chartValues.removeAll { it.myChartId == chartId }
    }

    override suspend fun deleteChartValueGreaterThanIndex(chartId: String, index: Int) {
        chartValues.removeAll { it.myChartId == chartId && it.index >= index }
    }
}
