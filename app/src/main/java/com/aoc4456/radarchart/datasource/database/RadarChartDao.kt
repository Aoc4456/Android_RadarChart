package com.aoc4456.radarchart.datasource.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RadarChartDao {

    /**
     * Create Group
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: ChartGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupLabel(label: ChartGroupLabel)

    @Transaction
    suspend fun saveGroupAndLabel(group: ChartGroup, labels: List<ChartGroupLabel>) {
        insertGroup(group)
        labels.forEach {
            insertGroupLabel(it)
        }
    }

    /**
     * Create Chart
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChart(chart: MyChart)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChartValue(chartValue: ChartValue)

    @Transaction
    suspend fun saveChartAndValues(chart: MyChart, values: List<ChartValue>) {
        insertChart(chart)
        values.forEach {
            insertChartValue(it)
        }
    }

    /**
     * Read
     */

    // TODO 並び順の管理
    @Transaction
    @Query("SELECT * FROM ChartGroup")
    fun observeGroupWithLabelAndCharts(): LiveData<List<GroupWithLabelAndCharts>>

    // TODO 並び順の管理
    @Transaction
    @Query("SELECT * FROM MyChart WHERE chartGroupId = :groupId")
    suspend fun getChartList(groupId: String): List<MyChartWithValue>

    /**
     * Update
     */
    @Update
    suspend fun updateGroup(group: ChartGroup)

    @Transaction
    suspend fun updateGroupAndLabel(
        group: ChartGroup,
        labels: List<ChartGroupLabel>,
        oldGroup: GroupWithLabelAndCharts
    ) {
        updateGroup(group)

        deleteGroupLabel(group.id)
        labels.forEach {
            insertGroupLabel(it)
        }

        val numberOfItemsDiff = labels.size - oldGroup.labelList.size
        if (numberOfItemsDiff > 0) { // 項目数増加
            val startIndex = labels.size
            val last = startIndex + numberOfItemsDiff

            oldGroup.chartList.forEach { chart ->
                for (i in startIndex until last) {
                    insertChartValue(
                        ChartValue(
                            myChartId = chart.myChart.id,
                            index = i,
                            value = group.maximumValue * 0.6
                        )
                    )
                }
            }
        }

        if (numberOfItemsDiff < 0) { // 項目数減少 TODO sortedIndex の 調整
            oldGroup.chartList.forEach { chart ->
                deleteChartValueGreaterThanIndex(chart.myChart.id, labels.size)
            }
        }
    }

    /**
     * Delete
     */

    @Query("DELETE FROM ChartGroup WHERE id = :groupId")
    suspend fun deleteGroup(groupId: String)

    @Query("DELETE FROM ChartGroupLabel WHERE chartGroupId = :groupId")
    suspend fun deleteGroupLabel(groupId: String)

    @Query("DELETE FROM MyChart WHERE id = :chartId")
    suspend fun deleteMyChart(chartId: String)

    @Query("DELETE FROM ChartValue WHERE myChartId = :chartId AND `index` >= :index")
    suspend fun deleteChartValueGreaterThanIndex(chartId: String, index: Int)
}
