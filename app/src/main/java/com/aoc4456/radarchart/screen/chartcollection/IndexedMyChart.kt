package com.aoc4456.radarchart.screen.chartcollection

import com.aoc4456.radarchart.datasource.database.MyChartWithValue

data class IndexedMyChart(
    var sortIndex: Int,
    var myChartWithValue: MyChartWithValue
)
