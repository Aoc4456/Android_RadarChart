package com.aoc4456.radarchart.util

import com.aoc4456.radarchart.datasource.database.MyChartWithValue
import com.aoc4456.radarchart.datasource.database.OrderBy
import com.aoc4456.radarchart.datasource.database.SortIndex

object MyChartOrder {
    fun getSortedChartList(
        list: List<MyChartWithValue>,
        sortIndex: Int,
        orderBy: OrderBy
    ): List<MyChartWithValue> {
        val sortedList: List<MyChartWithValue>
        when (sortIndex) {
            SortIndex.CREATED_AT -> {
                sortedList = when (orderBy) {
                    OrderBy.ASC -> {
                        list.sortedBy { it.myChart.createdAt }
                    }
                    OrderBy.DESC -> {
                        list.sortedByDescending { it.myChart.createdAt }
                    }
                }
            }
            SortIndex.UPDATED_AT -> {
                sortedList = when (orderBy) {
                    OrderBy.ASC -> {
                        list.sortedBy { it.myChart.updatedAt }
                    }
                    OrderBy.DESC -> {
                        list.sortedByDescending { it.myChart.updatedAt }
                    }
                }
            }
            SortIndex.SUM_OF_VALUES -> {
                sortedList = when (orderBy) {
                    OrderBy.ASC -> {
                        list.sortedBy { chart ->
                            chart.values.map { it.value }.sum()
                        }
                    }
                    OrderBy.DESC -> {
                        list.sortedByDescending { chart ->
                            chart.values.map { it.value }.sum()
                        }
                    }
                }
            }
            SortIndex.CHART_TITLE -> {
                sortedList = when (orderBy) {
                    OrderBy.ASC -> {
                        list.sortedBy { it.myChart.title }
                    }
                    OrderBy.DESC -> {
                        list.sortedByDescending { it.myChart.title }
                    }
                }
            }
            else -> {
                sortedList = when (orderBy) {
                    OrderBy.ASC -> {
                        list.sortedBy { it.values[sortIndex].value }
                    }
                    OrderBy.DESC -> {
                        list.sortedByDescending { it.values[sortIndex].value }
                    }
                }
            }
        }
        return sortedList
    }
}
