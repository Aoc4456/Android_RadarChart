package com.aoc4456.radarchart.screen.grouplistsort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupSortViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    lateinit var groupList: MutableList<GroupWithLabelAndCharts>

    private var isChanged = false

    private val _dismiss = MutableLiveData<Boolean>()
    val dismiss: LiveData<Boolean> = _dismiss

    fun onViewCreated(navArgs: GroupSortFragmentArgs) {
        groupList = navArgs.grouplist.toMutableList()
    }

    fun onMoveItem(from: Int, to: Int) {
        isChanged = true
        val moveItem = groupList[from]
        groupList.removeAt(from)
        groupList.add(to, moveItem)
    }

    fun onClickCloseButton() {
        if (isChanged) {
            viewModelScope.launch {
                repository.setGroupRates(groupList.map { it.group })
                _dismiss.value = true
            }
        } else {
            _dismiss.value = true
        }
    }
}
