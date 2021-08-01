package com.aoc4456.radarchart.screen.grouplist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.util.PublishLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    private val _groupList = repository.observeChartGroupList()
    val groupList: LiveData<List<GroupWithLabelAndCharts>> = _groupList

    private val _navigateToGroupEdit = PublishLiveData<GroupWithLabelAndCharts?>()
    val navigateToGroupEdit: PublishLiveData<GroupWithLabelAndCharts?> = _navigateToGroupEdit

    fun onClickListItem(position: Int) {
        Timber.d("クリックポジション = $position")
    }

    fun onSelectedContextMenu(position: Int, itemId: Int) {
        Timber.d("長押しポジション = $position  itemId = $itemId")
        when (itemId) {
            R.id.group_edit -> {
                _navigateToGroupEdit.value = groupList.value!![position]
            }
            R.id.sorting_items -> {
            }
        }
    }
}
