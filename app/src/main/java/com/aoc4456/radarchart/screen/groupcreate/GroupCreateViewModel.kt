package com.aoc4456.radarchart.screen.groupcreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aoc4456.radarchart.util.ChartDataUtil
import com.github.mikephil.charting.data.RadarData

class GroupCreateViewModel : ViewModel() {

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _numberOfItems = MutableLiveData(5)
    val numberOfItems: LiveData<Int> = _numberOfItems

    private val _groupColor = MutableLiveData(-14654801)
    val groupColor: LiveData<Int> = _groupColor

    private val _maximum = MutableLiveData(100)
    val maximum: LiveData<Int> = _maximum

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
        _title.value = "aaa" // TODO ここで編集画面の初期値とかを入力
        updateChart()
    }

    fun onSliderValueChanged(newValue: Float) {
        if (_numberOfItems.value == newValue.toInt()) return
        _numberOfItems.value = newValue.toInt()
        updateChart()
    }

    fun onChooseColor(newColor: Int) {
        if (newColor == _groupColor.value) return
        _groupColor.value = newColor
        updateChart()
    }

    fun onChangeTitleText(newText: String) {
        if (newText == title.value) return
        _title.value = newText
    }

    fun onChangeMaximumText(newMaximum: String) {
        if (newMaximum.toInt() == _maximum.value) return
        _maximum.value = newMaximum.toInt()
    }

    fun onTextChangeMultiEditText(index: Int, newText: String) {
        if (newText == itemTextList.value?.getOrNull(index)) return
        val newList = itemTextList.value!!
        newList[index] = newText
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
