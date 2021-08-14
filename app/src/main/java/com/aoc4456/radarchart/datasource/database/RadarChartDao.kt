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
        }
        if (numberOfItemsDiff < 0) { // 項目数減少
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
}
