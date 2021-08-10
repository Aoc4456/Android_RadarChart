package com.aoc4456.radarchart.screen.chartcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private val _chartLabels = MutableLiveData<List<String>>()
    val chartLabels: LiveData<List<String>> = _chartLabels

    private val _chartMaximum = MutableLiveData<Int>()
    val chartMaximum: LiveData<Int> = _chartMaximum

    private val _chartColor = MutableLiveData<Int>()
    val chartColor: LiveData<Int> = _chartColor

    private val _chartIntValue = MutableLiveData<List<Int>>()
    val chartIntValue: LiveData<List<Int>> = _chartIntValue

    // TODO MediatorLiveDataにする color と values のみ
    private val _chartData = MutableLiveData<RadarData>()
    val chartData: LiveData<RadarData> = _chartData

    private val _chartUpdate = MutableLiveData<Boolean>()
    val chartUpdate: LiveData<Boolean> = _chartUpdate

    fun onViewCreated(args: ChartCreateFragmentArgs) {
        if (groupData.value != null) return
        groupData.value = args.groupWithLabelAndCharts
        groupData.value?.let { groupData ->
            _chartLabels.value = groupData.labelList.map { it.text }
            _chartMaximum.value = groupData.group.maximumValue
        }

        if (args.chart == null) {
            _chartColor.value = groupData.value!!.group.color
            _chartIntValue.value =
                ChartDataUtil.getNPercentValues(chartMaximum.value!!, 60, chartLabels.value!!.size)
            _chartData.value = ChartDataUtil.getRadarDataWithTheSameValue(
                color = chartColor.value!!,
                numberOfItems = chartLabels.value!!.size,
                value = (chartMaximum.value!! * 0.6).toFloat()
            )
        } else {
            _chartArgs.value = args.chart
            chartArgs.value?.let {
                _chartColor.value = it.color
                // TODO chartValuesを代入
            }
        }
        _chartUpdate.value = true
    }

    fun onChooseColor(newColor: Int) {
        if (newColor == chartColor.value) return
        _chartColor.value = newColor
        updateChart()
    }

    private fun updateChart() {
        _chartData.value =
            ChartDataUtil.getRadarDataWithTheSameValue(
                color = chartColor.value!!,
                numberOfItems = chartLabels.value!!.size,
                value = (chartMaximum.value!! * 0.6).toFloat()
            )
        _chartUpdate.value = true
    }
}
