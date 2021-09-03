package com.aoc4456.radarchart.datasource

import androidx.lifecycle.LiveData
import com.aoc4456.radarchart.datasource.database.*
import com.aoc4456.radarchart.screen.chartcollection.CollectionType

interface RadarChartRepository {

    /**
     * Create
     */

    suspend fun saveGroup(
        group: ChartGroup,
        labels: List<String>,
        oldGroup: GroupWithLabelAndCharts?
    )

    suspend fun saveChart(chart: MyChart, values: List<Int>)

    /**
     * Read
     */

    fun observeGroupList(): LiveData<List<GroupWithLabelAndCharts>>

    suspend fun getGroupById(groupId: String): GroupWithLabelAndCharts

    suspend fun getSortedChartList(
        groupId: String,
        sortIndex: Int,
        orderBy: OrderBy
    ): List<MyChartWithValue>

    /**
     * Update
     */
    suspend fun changeAscDesc(groupId: String, orderBy: OrderBy)

    suspend fun updateSortIndex(groupId: String, sortIndex: Int)

    suspend fun setGroupRates(list: List<ChartGroup>)

    suspend fun swapGroupLabel(groupId: String, from: Int, to: Int)

    /**
     * Delete
     */

    suspend fun deleteGroup(groupId: String)

    suspend fun deleteMyChart(chartId: String)

    /**
     * Shared Preferences
     */

    fun saveCollectionType(type: CollectionType)

    fun loadCollectionType(): CollectionType
}
