package com.aoc4456.radarchart.screen.chartcollection

import androidx.lifecycle.ViewModel
import com.aoc4456.radarchart.datasource.RadarChartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChartCollectionViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    val viewType: ChartCollectionType = ChartCollectionType.LIST

    val maximum = 100
}
