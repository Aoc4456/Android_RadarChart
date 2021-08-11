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

    @Query("SELECT * FROM ChartGroup")
    fun getAllChartGroup(): List<ChartGroup>

    @Transaction
    @Query("SELECT * FROM ChartGroup")
    fun observeChartGroupForGroupList(): LiveData<List<GroupWithLabelAndCharts>>

    /**
     * Delete
     */

    @Query("DELETE FROM ChartGroup WHERE id = :groupId")
    suspend fun deleteChartGroup(groupId: String)
}
