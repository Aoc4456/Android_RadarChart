package com.aoc4456.radarchart.screen.chartcollection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.datasource.database.MyChartWithValue
import com.aoc4456.radarchart.datasource.database.OrderBy
import com.aoc4456.radarchart.util.MyChartOrder
import com.aoc4456.radarchart.util.PublishLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChartCollectionViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    lateinit var groupId: String

    private val _groupData = MutableLiveData<GroupWithLabelAndCharts>()
    val groupData: LiveData<GroupWithLabelAndCharts> = _groupData

    private val _chartList = MutableLiveData<List<MyChartWithValue>>()
    val chartList: LiveData<List<MyChartWithValue>> = _chartList

    private val _listOrGrid = MutableLiveData(CollectionType.LIST)
    val listOrGrid: LiveData<CollectionType> = _listOrGrid

    private val _navigateToChartEdit = PublishLiveData<MyChartWithValue?>()
    val navigateToChartEdit: PublishLiveData<MyChartWithValue?> = _navigateToChartEdit

    fun onViewCreated(navArgs: ChartCollectionFragmentArgs) {
        groupId = navArgs.groupWithLabelAndCharts!!.group.id
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) { fetchGroup() }
            _groupData.value = deferred.await()
            withContext(Dispatchers.IO) {
                fetchSortedChartList()
            }
        }
    }

    fun onClickCollectionItem(item: MyChartWithValue) {
        _navigateToChartEdit.value = item
    }

    fun onToggleButtonCheckedChanged(checkedId: Int) {
        when (checkedId) {
            R.id.toggleButtonList -> {
                _listOrGrid.value = CollectionType.LIST
            }
            R.id.toggleButtonGrid -> {
                _listOrGrid.value = CollectionType.GRID
            }
        }
    }

    fun onClickAscDescButton() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { updateAscDesc() }
            val deferred = async(Dispatchers.IO) { fetchGroup() }
            _groupData.value = deferred.await()
            reOrderChartList()
        }
    }

    private suspend fun fetchGroup(): GroupWithLabelAndCharts {
        return repository.getGroupById(groupId)
    }

    private suspend fun fetchSortedChartList() {
        _chartList.postValue(
            repository.getSortedChartList(
                groupId = groupData.value!!.group.id,
                sortIndex = groupData.value!!.group.sortIndex,
                orderBy = groupData.value!!.group.orderBy
            )
        )
    }

    private suspend fun updateAscDesc() {
        repository.changeAscDesc(
            groupId = groupData.value!!.group.id,
            orderBy = when (groupData.value!!.group.orderBy) {
                OrderBy.ASC -> {
                    OrderBy.DESC
                }
                else -> OrderBy.ASC
            }
        )
    }

    private fun reOrderChartList() {
        _chartList.value = MyChartOrder.getSortedChartList(
            list = chartList.value!!,
            sortIndex = groupData.value!!.group.sortIndex,
            orderBy = groupData.value!!.group.orderBy
        )
    }
}
