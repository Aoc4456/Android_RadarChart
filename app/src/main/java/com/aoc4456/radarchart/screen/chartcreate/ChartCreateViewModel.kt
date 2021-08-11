package com.aoc4456.radarchart.screen.chartcreate

import androidx.lifecycle.*
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.datasource.database.MyChart
import com.aoc4456.radarchart.util.ChartDataUtil
import com.aoc4456.radarchart.util.ValidateInputFieldUtil.titleValidate
import com.aoc4456.radarchart.util.ValidateResult
import com.github.mikephil.charting.data.RadarData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ChartCreateViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    private val groupData = MutableLiveData<GroupWithLabelAndCharts>()

    private val _chartArgs = MutableLiveData<MyChart>()
    val chartArgs: LiveData<MyChart> = _chartArgs

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _chartLabels = MutableLiveData<List<String>>()
    val chartLabels: LiveData<List<String>> = _chartLabels

    private val _chartMaximum = MutableLiveData<Int>()
    val chartMaximum: LiveData<Int> = _chartMaximum

    private val _chartColor = MutableLiveData<Int>()
    val chartColor: LiveData<Int> = _chartColor

    private val _chartIntValues = MutableLiveData<List<Int>>()
    val chartIntValues: LiveData<List<Int>> = _chartIntValues

    val chartData = MediatorLiveData<RadarData>().apply {
        addSource(chartColor) { color ->
            chartIntValues.value?.let { value = ChartDataUtil.getRadarDataFromValues(color, it) }
        }
        addSource(chartIntValues) { values ->
            chartColor.value?.let { value = ChartDataUtil.getRadarDataFromValues(it, values) }
        }
    }

    val total = chartIntValues.map { it.sum() }
    val average = chartIntValues.map { (it.average() * 10.0).roundToInt() / 10.0 }

    private val _comment = MutableLiveData<String>()
    val comment: LiveData<String> = _comment

    private val _chartUpdate = MutableLiveData<Boolean>()
    val chartUpdate: LiveData<Boolean> = _chartUpdate

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _errorMessage

    private val _dismiss = MutableLiveData<Boolean>()
    val dismiss: LiveData<Boolean> = _dismiss

    fun onViewCreated(args: ChartCreateFragmentArgs) {
        if (groupData.value != null) return
        groupData.value = args.groupWithLabelAndCharts
        groupData.value?.let { groupData ->
            _chartLabels.value = groupData.labelList.map { it.text }
            _chartMaximum.value = groupData.group.maximumValue
        }

        if (args.chart == null) {
            _chartColor.value = groupData.value!!.group.color
            _chartIntValues.value =
                ChartDataUtil.getNPercentValues(chartMaximum.value!!, 60, chartLabels.value!!.size)
        } else {
            _chartArgs.value = args.chart
            chartArgs.value?.let {
                _chartColor.value = it.color
                // TODO chartValuesを代入
            }
        }
        _chartUpdate.value = true
    }

    fun onChangeTitleText(newText: String) {
        if (newText == title.value) return
        _title.value = newText
    }

    fun onChooseColor(newColor: Int) {
        if (newColor == chartColor.value) return
        _chartColor.value = newColor
        _chartUpdate.value = true
    }

    fun onChangeChartIntValue(index: Int, newValue: Int) {
        if (newValue == chartIntValues.value?.getOrNull(index)) return
        val tempList = chartIntValues.value!!.toMutableList()
        tempList[index] = newValue
        _chartIntValues.value = tempList
        _chartUpdate.value = true
    }

    fun onChangeComment(newText: String) {
        if (newText == comment.value) return
        _comment.value = newText
    }

    fun onClickSaveButton() {
        // バリデーション
        val validateResult = validateInputField()
        val validateFail = !validateResult.first
        if (validateFail) {
            _errorMessage.value = validateResult.second!!
            return
        }
        // Entity を作成

        // 保存

        _dismiss.value = true
    }

    private fun validateInputField(): Pair<Boolean, Int?> {
        val titleValidateResult = titleValidate(title.value!!)
        if (titleValidateResult != ValidateResult.SUCCESS) {
            return Pair(false, titleValidateResult.stringResId)
        }
        return Pair(true, null)
    }
}
