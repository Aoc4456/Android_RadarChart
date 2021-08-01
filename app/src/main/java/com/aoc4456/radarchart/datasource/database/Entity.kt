package com.aoc4456.radarchart.datasource.database

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Roomのテーブルを表すクラスたち
 */

@Parcelize
@Entity
data class ChartGroup(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var color: Int,
    var iconFileName: String = "",
    var maximumValue: Int = 0,
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),
    var sortIndex: Int = SortIndex.CREATED_AT,
    var orderBy: OrderBy = OrderBy.ASC
) : Parcelable

@Parcelize
@Entity(primaryKeys = ["chartGroupId", "index"])
data class ChartGroupLabel(
    var chartGroupId: String = "",
    var index: Int = 0,
    var text: String = ""
) : Parcelable

@Parcelize
@Entity
data class MyChart(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var chartGroupId: String = "",
    var title: String = "",
    var color: Int,
    var comment: String = "",
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
) : Parcelable

@Entity(primaryKeys = ["myChartId", "index"])
data class ChartValue(
    var myChartId: String = "",
    var index: Int = 0,
    var value: Double = 0.0
)

/**
 * １対多のリレーション
 */
@Parcelize
data class GroupWithLabelAndCharts(
    @Embedded val group: ChartGroup,
    @Relation(
        parentColumn = "id",
        entityColumn = "chartGroupId"
    )
    val labelList: List<ChartGroupLabel>,
    @Relation(
        parentColumn = "id",
        entityColumn = "chartGroupId"
    )
    val chartList: List<MyChart>
) : Parcelable

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
