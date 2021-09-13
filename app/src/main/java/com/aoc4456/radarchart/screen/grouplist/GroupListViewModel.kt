package com.aoc4456.radarchart.screen.grouplist

import androidx.lifecycle.*
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.util.PublishLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    private val _groupList = MutableLiveData<List<GroupWithLabelAndCharts>>()
    val groupList: LiveData<List<GroupWithLabelAndCharts>> = _groupList

    private val _navigateToGroupEdit = PublishLiveData<GroupWithLabelAndCharts?>()
    val navigateToGroupEdit: PublishLiveData<GroupWithLabelAndCharts?> = _navigateToGroupEdit

    private val _navigateToItemSort = PublishLiveData<GroupWithLabelAndCharts?>()
    val navigateToItemSort: PublishLiveData<GroupWithLabelAndCharts?> = _navigateToItemSort

    private val _navigateToChartCollection = PublishLiveData<GroupWithLabelAndCharts?>()
    val navigateToChartCollection: PublishLiveData<GroupWithLabelAndCharts?> =
        _navigateToChartCollection

    val sortButtonEnable: LiveData<Boolean> = groupList.map {
        it.size >= 2
    }

    fun onViewCreated() {
        viewModelScope.launch {
            _groupList.value = repository.getGroupList()
        }
    }

    fun onClickListItem(groupItem: GroupWithLabelAndCharts) {
        _navigateToChartCollection.value = groupItem
    }

    fun onSelectedContextMenu(groupItem: GroupWithLabelAndCharts, itemId: Int) {
        when (itemId) {
            R.id.group_edit -> {
                _navigateToGroupEdit.value = groupItem
            }
            R.id.sorting_items -> {
                _navigateToItemSort.value = groupItem
            }
        }
    }
}
