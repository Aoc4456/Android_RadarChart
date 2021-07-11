package com.aoc4456.radarchart.datasource.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

/**
 * Roomのテーブルを表すクラスたち
 */

@Entity
data class ChartGroup(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var colorString: String = "",
    var iconFileName: String = "",
    var maximumValue: Double = 0.0,
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),
    var sortIndex: Int = SortIndex.CREATED_AT,
    var orderBy: OrderBy = OrderBy.ASC
)

@Entity(primaryKeys = ["chartGroupId", "index"])
data class ChartGroupLabel(
    var chartGroupId: String = "",
    var index: Int = 0,
    var text: String = ""
)

@Entity
data class MyChart(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var colorString: String = "",
    var comment: String = "",
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
)

@Entity(primaryKeys = ["myChartId", "index"])
data class ChartValue(
    var myChartId: String = "",
    var index: Int = 0,
    var value: Double = 0.0
)

class Converters {
    @TypeConverter
    fun toOrderBy(value: String) = enumValueOf<OrderBy>(value)

    @TypeConverter
    fun fromOrderBy(value: OrderBy) = value.name
}

enum class OrderBy {
    ASC,
    DESC
}

object SortIndex {
    const val CREATED_AT = -1
    const val UPDATED_AT = -2
    const val SUM_OF_VALUES = -3
    const val CHART_TITLE = -4
}
