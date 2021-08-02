package com.aoc4456.radarchart.datasource.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RadarChartDao {

    /**
     * Create
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
                group.id,
                i,
                labels[i]
            )
            insertChartGroupLabel(groupLabel)
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
