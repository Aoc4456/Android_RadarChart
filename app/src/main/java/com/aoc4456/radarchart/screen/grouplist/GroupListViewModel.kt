package com.aoc4456.radarchart.screen.grouplist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    private val _groupList = repository.observeChartGroupList()
    val groupList: LiveData<List<GroupWithLabelAndCharts>> = _groupList
}
