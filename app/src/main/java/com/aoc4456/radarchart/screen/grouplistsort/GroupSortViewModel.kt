package com.aoc4456.radarchart.screen.grouplistsort

import androidx.lifecycle.ViewModel
import com.aoc4456.radarchart.datasource.RadarChartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupSortViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel()
