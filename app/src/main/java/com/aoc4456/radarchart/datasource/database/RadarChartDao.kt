package com.aoc4456.radarchart.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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
}
