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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemSortViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    lateinit var group: GroupWithLabelAndCharts
    lateinit var labelList: MutableList<ChartGroupLabel>

    private val _listSetup = MutableLiveData<Boolean>()
    val listSetup: LiveData<Boolean> = _listSetup

    private val _chartUpdate = MutableLiveData<Boolean>()
    val chartUpdate: LiveData<Boolean> = _chartUpdate

    val radarData: RadarData
        get() {
            return ChartDataUtil.getRadarDataWithTheSameValue(
                color = group.group.color,
                numberOfItems = group.labelList.size
            )
        }

    fun onViewCreated(navArgs: ItemSortFragmentArgs) {
        if (::group.isInitialized) return
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) {
                repository.getGroupById(navArgs.groupWithLabelAndCharts.group.id)
            }
            group = deferred.await()
            labelList = group.labelList.toMutableList()
            _listSetup.value = true
            _chartUpdate.value = true
        }
    }

    fun onMoveItem(from: Int, to: Int) {
        val moveItem = labelList[from]
        labelList.removeAt(from)
        labelList.add(to, moveItem)
        viewModelScope.launch {
            repository.swapGroupLabel(group.group.id, from, to)
        }
        _chartUpdate.value = true
    }
}
