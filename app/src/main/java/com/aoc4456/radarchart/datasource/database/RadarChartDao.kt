package com.aoc4456.radarchart.datasource.database

import androidx.room.*

@Dao
interface RadarChartDao {

    /**
     * Insert a ChartGroup in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChartGroup(group: ChartGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChartGroupLabel(label: ChartGroupLabel)

    @Query("SELECT * FROM ChartGroup")
    fun getAllChartGroup(): List<ChartGroup>

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
}
