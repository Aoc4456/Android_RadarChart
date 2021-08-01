package com.aoc4456.radarchart.screen.groupcreate

import androidx.lifecycle.*
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.ChartGroup
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.util.ChartDataUtil
import com.aoc4456.radarchart.util.ValidateInputFieldUtil.maximumValidate
import com.aoc4456.radarchart.util.ValidateInputFieldUtil.titleValidate
import com.aoc4456.radarchart.util.ValidateResult
import com.github.mikephil.charting.data.RadarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupCreateViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    private val _groupArgs = MutableLiveData<GroupWithLabelAndCharts?>()
    val groupArgs: LiveData<GroupWithLabelAndCharts?> = _groupArgs

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _numberOfItems = MutableLiveData(5)
    val numberOfItems: LiveData<Int> = _numberOfItems

    private val _groupColor = MutableLiveData(-14654801)
    val groupColor: LiveData<Int> = _groupColor

    private val _maximum = MutableLiveData("100")
    val maximum: LiveData<String> = _maximum

    private var itemTextList =
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

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _errorMessage

    private val _dismiss = MutableLiveData<Boolean>()
    val dismiss: LiveData<Boolean> = _dismiss

    fun onViewCreated(groupArgs: GroupWithLabelAndCharts?) {
        if (groupArgs != null) {
            _groupArgs.value = groupArgs
            _title.value = groupArgs.group.title
            _groupColor.value = groupArgs.group.color
            _maximum.value = groupArgs.group.maximumValue.toString()
            itemTextList.value = groupArgs.labelList.map { it.text }.toMutableList()
            _numberOfItems.value = groupArgs.labelList.size
        }
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
        if (newMaximum == _maximum.value) return
        _maximum.value = newMaximum
    }

    fun onTextChangeMultiEditText(index: Int, newText: String) {
        if (newText == itemTextList.value?.getOrNull(index)) return
        val newList = itemTextList.value!!
        newList[index] = newText
        itemTextList.value = newList
        updateChart()
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
        val entity = createEntity()
        viewModelScope.launch {
            repository.saveGroup(entity.first, entity.second)
        }

        _dismiss.value = true
    }

    private fun updateChart() {
        _chartData.value =
            ChartDataUtil.getRadarDataWithTheSameValue(groupColor.value!!, numberOfItems.value!!)
        _chartUpdate.value = true
    }

    private fun validateInputField(): Pair<Boolean, Int?> {
        val titleValidateResult = titleValidate(title.value!!)
        if (titleValidateResult != ValidateResult.SUCCESS) {
            return Pair(false, titleValidateResult.stringResId)
        }

        val maximumValidateResult = maximumValidate(maximum.value!!)
        if (maximumValidateResult != ValidateResult.SUCCESS) {
            return Pair(false, maximumValidateResult.stringResId)
        }

        return Pair(true, null)
    }

    private fun createEntity(): Pair<ChartGroup, List<String>> {
        val group = ChartGroup(
            title = title.value!!,
            color = groupColor.value!!,
            maximumValue = maximum.value!!.toInt(),
        )
        val labels = exactlySizedTextList.value!!
        return Pair(group, labels)
    }
}
