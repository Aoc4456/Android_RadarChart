package com.aoc4456.radarchart.screen.groupcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aoc4456.radarchart.util.ChartDataUtil
import com.github.mikephil.charting.data.RadarData

class GroupCreateViewModel : ViewModel() {

    private val _numberOfItems = MutableLiveData(5)
    val numberOfItems: LiveData<Int> = _numberOfItems

    private val _groupColor = MutableLiveData(-14654801)
    val groupColor: LiveData<Int> = _groupColor

    private val itemTextList = mutableListOf("項目1", "項目2", "項目3", "項目4", "項目5", "項目6", "項目7", "項目8")

    private val _exactlySizedTextList = MutableLiveData<List<String>>()
    val exactlySizedTextList: LiveData<List<String>> = _exactlySizedTextList

    private val _chartData = MutableLiveData<Pair<RadarData, List<String>>>()
    val chartData: LiveData<Pair<RadarData, List<String>>> = _chartData

    fun onViewCreated() {
        _exactlySizedTextList.value =
            GroupCreateUtil.getExactlySizedTextList(itemTextList, numberOfItems.value!!)
        updateChart()
    }

    fun onSliderValueChanged(value: Float) {
        _numberOfItems.value = value.toInt()
        _exactlySizedTextList.value =
            GroupCreateUtil.getExactlySizedTextList(itemTextList, value.toInt())
        updateChart()
    }

    fun onChooseColor(color: Int) {
        _groupColor.value = color
        updateChart()
    }

    fun onEndEditingMultiEditText(index: Int, text: String) {
        itemTextList[index] = text
        updateChart()
    }

    private fun updateChart() {
        val radarData =
            ChartDataUtil.getRadarDataWithTheSameValue(groupColor.value!!, numberOfItems.value!!)
        _chartData.value = Pair(radarData, exactlySizedTextList.value!!)
    }
}
