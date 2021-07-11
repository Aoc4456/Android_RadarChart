package com.aoc4456.radarchart.datasource.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RadarChartDao {
    @Query("SELECT * FROM ChartGroup")
    fun getAllChartGroup(): List<ChartGroup>
}
