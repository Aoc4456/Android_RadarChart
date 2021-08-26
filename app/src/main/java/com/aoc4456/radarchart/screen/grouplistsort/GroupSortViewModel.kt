package com.aoc4456.radarchart.screen.grouplistsort

import androidx.lifecycle.ViewModel
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupSortViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    lateinit var groupList: MutableList<GroupWithLabelAndCharts>

    fun onViewCreated(navArgs: GroupSortFragmentArgs) {
        groupList = navArgs.grouplist.toMutableList()
    }

    fun onMoveItem(from: Int, to: Int) {
        val moveItem = groupList[from]
        groupList.removeAt(from)
        groupList.add(to, moveItem)
    }
}
