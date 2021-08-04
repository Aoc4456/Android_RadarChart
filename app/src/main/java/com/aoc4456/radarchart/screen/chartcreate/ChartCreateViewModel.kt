package com.aoc4456.radarchart.screen.chartcreate

import androidx.lifecycle.ViewModel
import com.aoc4456.radarchart.datasource.RadarChartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChartCreateViewModel @Inject constructor(
    private val repository: RadarChartRepository
) : ViewModel()
