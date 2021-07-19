package com.aoc4456.radarchart.screen.groupcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aoc4456.radarchart.util.ChartDataUtil
import com.github.mikephil.charting.data.RadarData

class GroupCreateViewModel : ViewModel() {

    private var title = ""

    private val _numberOfItems = MutableLiveData(5)
    val numberOfItems: LiveData<Int> = _numberOfItems

    private val _groupColor = MutableLiveData(-14654801)
    val groupColor: LiveData<Int> = _groupColor

    private val itemTextList = mutableListOf("項目1", "項目2", "項目3", "項目4", "項目5", "項目6", "項目7", "項目8")

    // TODO できればこんなフィールドは作らないでやる
    private val _exactlySizedTextList = MutableLiveData<List<String>>()
    val exactlySizedTextList: LiveData<List<String>> = _exactlySizedTextList

    private val _chartData = MutableLiveData<Pair<RadarData, List<String>>>()
    val chartData: LiveData<Pair<RadarData, List<String>>> = _chartData

    private var maximum = 100

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

    fun onChangeTitleText(text: String) {
        title = text
    }

    fun onChangeMaximumText(text: String) {
        maximum = text.toInt()
    }

    fun onTextChangeMultiEditText(index: Int, text: String) {
        itemTextList[index] = text
        _exactlySizedTextList.value =
            GroupCreateUtil.getExactlySizedTextList(itemTextList, numberOfItems.value!!)
        updateChart()
    }

    private fun updateChart() {
        val radarData =
            ChartDataUtil.getRadarDataWithTheSameValue(groupColor.value!!, numberOfItems.value!!)
        _chartData.value = Pair(radarData, exactlySizedTextList.value!!)
    }

    fun onClickSaveButton() {
        // 値のvalidate -> 処理を別のクラスに委譲する

        // 保存
    }
}
