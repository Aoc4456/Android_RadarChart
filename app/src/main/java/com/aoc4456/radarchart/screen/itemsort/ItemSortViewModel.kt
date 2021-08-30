package com.aoc4456.radarchart.screen.itemsort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.ChartGroupLabel
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemSortViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    lateinit var group: GroupWithLabelAndCharts
    lateinit var labelList: MutableList<ChartGroupLabel>

    fun onViewCreated(navArgs: ItemSortFragmentArgs) {
        group = navArgs.groupWithLabelAndCharts // TODO 毎回データベースから取得しないと危険？
        labelList = navArgs.groupWithLabelAndCharts.labelList.toMutableList()
    }

    fun onMoveItem(from: Int, to: Int) {
        val moveItem = labelList[from]
        labelList.removeAt(from)
        labelList.add(to, moveItem)
        viewModelScope.launch {
            repository.swapGroupLabel(group.group.id, from, to)
        }
    }
}
