package com.aoc4456.radarchart.screen.chartcollection

import androidx.lifecycle.ViewModel
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChartCollectionViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    lateinit var groupData: GroupWithLabelAndCharts

    val viewType: ChartCollectionType = ChartCollectionType.LIST

    val maximum = 100

    fun onViewCreated(navArgs: ChartCollectionFragmentArgs) {
        groupData = navArgs.groupWithLabelAndCharts!!
    }
}
