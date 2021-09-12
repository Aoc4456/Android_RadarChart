package com.aoc4456.radarchart.screen.itemsort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.ChartGroupLabel
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.util.ChartDataUtil
import com.github.mikephil.charting.data.RadarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemSortViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    lateinit var group: GroupWithLabelAndCharts
    lateinit var labelList: MutableList<ChartGroupLabel>

    private val _chartUpdate = MutableLiveData<Boolean>()
    val chartUpdate: LiveData<Boolean> = _chartUpdate

    private val _dismiss = MutableLiveData<Boolean>()
    val dismiss: LiveData<Boolean> = _dismiss

    val radarData: RadarData
        get() {
            return ChartDataUtil.getRadarDataWithTheSameValue(
                color = group.group.color,
                numberOfItems = group.labelList.size
            )
        }

    private var isChanged = false

    fun onViewCreated(navArgs: ItemSortFragmentArgs) {
        if (::group.isInitialized) return
        group = navArgs.groupWithLabelAndCharts
        labelList = group.labelList.toMutableList()
        _chartUpdate.value = true
    }

    fun onMoveItem(from: Int, to: Int) {
        isChanged = true
        val moveItem = labelList[from]
        labelList.removeAt(from)
        labelList.add(to, moveItem)
        _chartUpdate.value = true
    }

    fun onClickCloseButton() {
        if (isChanged) {
            viewModelScope.launch {
                // TODO 並び替え処理
                _dismiss.value = true
            }
        } else {
            _dismiss.value = true
        }
    }
}
