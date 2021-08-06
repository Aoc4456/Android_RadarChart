package com.aoc4456.radarchart.screen.chartcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.datasource.database.MyChart
import com.aoc4456.radarchart.util.ChartDataUtil
import com.github.mikephil.charting.data.RadarData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChartCreateViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    private val groupData = MutableLiveData<GroupWithLabelAndCharts>()

    private val _chartArgs = MutableLiveData<MyChart>()
    val chartArgs: LiveData<MyChart> = _chartArgs

    val chartLabels: LiveData<List<String>> = groupData.map { group ->
        group.labelList.map { it.text }
    }

    val chartMaximum: LiveData<Int> = groupData.map { it.group.maximumValue }

    private val _chartColor = MutableLiveData<Int>()
    val chartColor: LiveData<Int> = _chartColor

    private val _chartData = MutableLiveData<RadarData>()
    val chartData: LiveData<RadarData> = _chartData

    private val _chartUpdate = MutableLiveData<Boolean>()
    val chartUpdate: LiveData<Boolean> = _chartUpdate

    fun onViewCreated(args: ChartCreateFragmentArgs) {
        groupData.value = args.groupWithLabelAndCharts
        if (args.chart == null) {
            _chartColor.value = groupData.value!!.group.color
            _chartData.value = ChartDataUtil.getRadarDataWithTheSameValue(
                color = chartColor.value!!,
                numberOfItems = groupData.value!!.labelList.size,
                value = (groupData.value!!.group.maximumValue * 0.6).toFloat()
            )
        } else {
            _chartArgs.value = args.chart
            chartArgs.value?.let {
                _chartColor.value = it.color
            }
        }
        _chartUpdate.value = true
    }
}
