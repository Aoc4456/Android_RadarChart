package com.aoc4456.radarchart.screen.chartcollection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoc4456.radarchart.datasource.RadarChartRepository
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.datasource.database.MyChartWithValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChartCollectionViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel() {

    private val _groupData = MutableLiveData<GroupWithLabelAndCharts>()
    val groupData: LiveData<GroupWithLabelAndCharts> = _groupData

    private val _chartList = MutableLiveData<List<MyChartWithValue>>()
    val chartList: LiveData<List<MyChartWithValue>> = _chartList

    fun onViewCreated(navArgs: ChartCollectionFragmentArgs) {
        _groupData.value = navArgs.groupWithLabelAndCharts!!
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _chartList.postValue(repository.getChartList(groupData.value!!.group.id))
            }
        }
    }
}
