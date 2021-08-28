package com.aoc4456.radarchart.screen.chartcollection

import android.content.Context
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.datasource.database.MyChartWithValue
import com.aoc4456.radarchart.datasource.database.SortIndex
import com.aoc4456.radarchart.util.DateUtil

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

    fun getLabelForComment2(
        context: Context,
        group: GroupWithLabelAndCharts,
        myChart: MyChartWithValue
    ): String {
        when (val sortIndex = group.group.sortIndex) {
            SortIndex.CHART_TITLE -> return ""
            SortIndex.SUM_OF_VALUES -> return ""
            SortIndex.CREATED_AT -> {
                val dateString = DateUtil.getLocalDateStringFromMilliSecond(
                    context = context,
                    milliSecond = myChart.myChart.createdAt
                )
                return context.resources.getString(
                    R.string.created_at_with_value,
                    dateString
                )
            }
            SortIndex.UPDATED_AT -> {
                val dateString = DateUtil.getLocalDateStringFromMilliSecond(
                    context = context,
                    milliSecond = myChart.myChart.updatedAt
                )
                return context.resources.getString(
                    R.string.updated_at_with_value,
                    dateString
                )
            }
            else -> {
                val label = group.labelList.getOrNull(sortIndex)?.text ?: ""
                val value = myChart.values.getOrNull(sortIndex)?.value?.toInt() ?: 0
                return "$label  :  $value"
            }
        }
    }
}
