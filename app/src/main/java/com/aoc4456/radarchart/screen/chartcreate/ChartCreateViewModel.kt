package com.aoc4456.radarchart.screen.chartcreate

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.aoc4456.radarchart.component.dialog.DialogButtonType
import com.aoc4456.radarchart.component.dialog.DialogType
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.datasource.database.MyChart
import com.aoc4456.radarchart.datasource.database.MyChartWithValue
import com.aoc4456.radarchart.util.ChartDataUtil
import com.aoc4456.radarchart.util.ImageUtil
import com.aoc4456.radarchart.util.ValidateInputFieldUtil.titleValidate
import com.aoc4456.radarchart.util.ValidateResult
import com.github.mikephil.charting.data.RadarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ChartCreateViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    private val groupData = MutableLiveData<GroupWithLabelAndCharts>()

    private val _chartArgs = MutableLiveData<MyChartWithValue>()
    val chartArgs: LiveData<MyChartWithValue> = _chartArgs

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

    val valuedLabel = MediatorLiveData<List<String>>().apply {
        addSource(chartLabels) { labels ->
            value = ChartCreateUtil.getValuedLabelList(labels, chartIntValues.value)
        }
        addSource(chartIntValues) { values ->
            value = ChartCreateUtil.getValuedLabelList(chartLabels.value, values)
        }
    }

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

    private val _comment = MutableLiveData("")
    val comment: LiveData<String> = _comment

    private val _chartUpdate = MutableLiveData<Boolean>()
    val chartUpdate: LiveData<Boolean> = _chartUpdate

    private val _iconImage = MutableLiveData<Bitmap?>()
    val iconImage: LiveData<Bitmap?> = _iconImage

    private val iconImageByteArray: ByteArray?
        get() {
            if (iconImage.value == null) return null
            return ImageUtil.bitmapToByteArray(iconImage.value!!)
        }

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
            chartArgs.value?.let { chartWithValue ->
                _title.value = chartWithValue.myChart.title
                _chartColor.value = chartWithValue.myChart.color
                _chartIntValues.value = chartWithValue.values.map { it.value.toInt() }
                _comment.value = chartWithValue.myChart.comment
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

        // 保存
        val saveData = createEntity()
        viewModelScope.launch {
            repository.saveChart(saveData.first, saveData.second)
        }

        _dismiss.value = true
    }

    fun onClickButtonInDialog(dialogType: DialogType, buttonType: DialogButtonType) {
        when (buttonType) {
            DialogButtonType.POSITIVE -> {
                if (chartArgs.value == null) return
                viewModelScope.launch {
                    repository.deleteMyChart(chartArgs.value!!.myChart.id)
                }
                _dismiss.value = true
            }
            DialogButtonType.NEGATIVE -> {
            }
        }
    }

    private fun validateInputField(): Pair<Boolean, Int?> {
        val titleValidateResult = titleValidate(title.value!!)
        if (titleValidateResult != ValidateResult.SUCCESS) {
            return Pair(false, titleValidateResult.stringResId)
        }
        return Pair(true, null)
    }

    private fun createEntity(): Pair<MyChart, List<Int>> {
        val myChart = createMyChart()
        val values = chartIntValues.value!!
        return Pair(myChart, values)
    }

    private fun createMyChart(): MyChart {
        val isNew = chartArgs.value == null

        if (isNew) {
            return MyChart(
                chartGroupId = groupData.value!!.group.id,
                title = title.value!!,
                color = chartColor.value!!,
                comment = comment.value!!
            )
        }

        val oldChart = chartArgs.value!!.myChart
        return oldChart.also {
            it.title = title.value!!
            it.color = chartColor.value!!
            it.comment = comment.value!!
            it.updatedAt = System.currentTimeMillis()
        }
    }
}
