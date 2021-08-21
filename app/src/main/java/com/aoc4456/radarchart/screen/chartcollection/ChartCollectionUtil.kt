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

    fun getItemsForSortDialog(labels: List<String>, context: Context): List<String> {
        val list = mutableListOf<String>()
        list.add(context.getString(R.string.total_value))
        labels.forEach {
            list.add(it)
        }
        list.add(context.getString(R.string.title))
        list.add(context.getString(R.string.created_at))
        list.add(context.getString(R.string.updated_at))
        return list
    }

    fun convertSelectedItemToSortIndex(selectedIndex: Int, labelSize: Int): Int {
        if (selectedIndex == 0) return SortIndex.SUM_OF_VALUES
        if (selectedIndex <= labelSize) return selectedIndex - 1
        return when (selectedIndex - 1 - labelSize) {
            0 -> SortIndex.CHART_TITLE
            1 -> SortIndex.CREATED_AT
            2 -> SortIndex.UPDATED_AT
            else -> SortIndex.CREATED_AT
        }
    }
}
