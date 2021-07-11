package com.aoc4456.radarchart.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ChartGroup::class, ChartGroupLabel::class, MyChart::class, ChartValue::class],
    version = 1
)
abstract class RadarChartDatabase : RoomDatabase() {
    abstract fun radarChartDao(): RadarChartDao
}
