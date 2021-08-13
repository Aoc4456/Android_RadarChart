package com.aoc4456.radarchart.screen.chartcollection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChartCollectionViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    private val _groupData = MutableLiveData<GroupWithLabelAndCharts>()
    val groupData: LiveData<GroupWithLabelAndCharts> = _groupData

    val maximum = 100

    fun onViewCreated(navArgs: ChartCollectionFragmentArgs) {
        _groupData.value = navArgs.groupWithLabelAndCharts!!
    }
}
