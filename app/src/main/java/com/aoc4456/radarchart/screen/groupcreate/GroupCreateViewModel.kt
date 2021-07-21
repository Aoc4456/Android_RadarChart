package com.aoc4456.radarchart.screen.groupcreate

import androidx.lifecycle.*
import com.aoc4456.radarchart.util.ChartDataUtil
import com.github.mikephil.charting.data.RadarData

class GroupCreateViewModel : ViewModel() {

    private var title = ""

    private val _numberOfItems = MutableLiveData(5)
    val numberOfItems: LiveData<Int> = _numberOfItems

    private val _groupColor = MutableLiveData(-14654801)
    val groupColor: LiveData<Int> = _groupColor

    private var maximum = 100

    private val itemTextList =
        MutableLiveData(mutableListOf("項目1", "項目2", "項目3", "項目4", "項目5", "項目6", "項目7", "項目8"))

    val exactlySizedTextList = MediatorLiveData<List<String>>().apply {
        addSource(numberOfItems) {
            value = GroupCreateUtil.getExactlySizedTextList(itemTextList.value!!, it)
        }
        addSource(itemTextList) {
            value = GroupCreateUtil.getExactlySizedTextList(it, numberOfItems.value!!)
        }
    }

    private val _chartData = MutableLiveData<RadarData>()
    val chartData: LiveData<RadarData> = _chartData

    private val _chartUpdate = MutableLiveData<Boolean>()
    val chartUpdate: LiveData<Boolean> = _chartUpdate

    fun onViewCreated() {
        updateChart()
    }

    fun onSliderValueChanged(value: Float) {
        _numberOfItems.value = value.toInt()
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
        val newList = itemTextList.value!!
        newList[index] = text
        itemTextList.value = newList
        updateChart()
    }

    fun onClickSaveButton() {
        // 値のvalidate -> 処理を別のクラスに委譲する

        // 保存
    }

    private fun updateChart() {
        _chartData.value =
            ChartDataUtil.getRadarDataWithTheSameValue(groupColor.value!!, numberOfItems.value!!)
        _chartUpdate.value = true
    }
}
