package com.aoc4456.radarchart.screen.chartcollection

import android.content.Context
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.datasource.database.SortIndex

object ChartCollectionUtil {
    fun sortIndexToString(sortIndex: Int, labels: List<String>, context: Context): String {
        return when (sortIndex) {
            SortIndex.CREATED_AT -> {
                context.getString(R.string.created_at)
            }
            SortIndex.UPDATED_AT -> {
                context.getString(R.string.updated_at)
            }
            SortIndex.SUM_OF_VALUES -> {
                context.getString(R.string.total_value)
            }
            SortIndex.CHART_TITLE -> {
                context.getString(R.string.title)
            }
            else -> {
                labels[sortIndex]
            }
        }
    }
}
