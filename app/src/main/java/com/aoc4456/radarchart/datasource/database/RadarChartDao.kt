package com.aoc4456.radarchart.datasource.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RadarChartDao {

    /**
     * Create Group
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChartGroup(group: ChartGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChartGroupLabel(label: ChartGroupLabel)

    @Transaction
    suspend fun saveChartGroupAndLabel(group: ChartGroup, labels: List<String>) {
        insertChartGroup(group)
        for (i in labels.indices) {
            val groupLabel = ChartGroupLabel(
                chartGroupId = group.id,
                index = i,
                text = labels[i]
            )
            insertChartGroupLabel(groupLabel)
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
    suspend fun saveChartAndValues(chart: MyChart, values: List<Int>) {
        insertChart(chart)
        for (i in values.indices) {
            val chartValues = ChartValue(
                myChartId = chart.id,
                index = i,
                value = values[i].toDouble()
            )
            insertChartValue(chartValues)
        }
    }

    /**
     * Read
     */

    // TODO 並び順の管理
    @Transaction
    @Query("SELECT * FROM ChartGroup")
    fun observeChartGroupForGroupList(): LiveData<List<GroupWithLabelAndCharts>>

    // TODO 並び順の管理
    @Transaction
    @Query("SELECT * FROM MyChart WHERE chartGroupId = :groupId ORDER BY createdAt ASC")
    suspend fun getChartList(groupId: String): List<MyChartWithValue>

    /**
     * Update
     */
    @Update
    suspend fun updateGroup(group: ChartGroup)

    @Update
    suspend fun updateGroupLabel(labels: ChartGroupLabel)

    @Transaction
    suspend fun updateChartGroupAndLabel(
        group: ChartGroup,
        labels: List<String>,
        oldGroup: GroupWithLabelAndCharts
    ) {
        updateGroup(group)

        deleteGroupLabel(group.id)
        for (i in labels.indices) {
            val groupLabel = ChartGroupLabel(
                chartGroupId = group.id,
                index = i,
                text = labels[i]
            )
            insertChartGroupLabel(groupLabel)
        }

        // 個数が増えた場合、グループに属するチャート全てに、初期値でValueを追加する

        // 個数が減った場合
    }

    /**
     * Delete
     */

    @Query("DELETE FROM ChartGroup WHERE id = :groupId")
    suspend fun deleteChartGroup(groupId: String)

    @Query("DELETE FROM ChartGroupLabel WHERE chartGroupId = :groupId")
    suspend fun deleteGroupLabel(groupId: String)

    @Query("DELETE FROM MyChart WHERE id = :chartId")
    suspend fun deleteMyChart(chartId: String)
}
