package com.aoc4456.radarchart.component.chart.radardataset

import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class BaseRadarDataSet(entryList: List<RadarEntry>) : RadarDataSet(entryList, "") {

    init {
        setDrawFilled(true)
        valueFormatter = IndexAxisValueFormatter()
    }

    fun setColorAndFillColor(newColor: Int) {
        color = newColor
        fillColor = newColor
    }

    override fun getEntryIndex(e: RadarEntry?): Int {
        TODO("Not yet implemented")
    }
}
