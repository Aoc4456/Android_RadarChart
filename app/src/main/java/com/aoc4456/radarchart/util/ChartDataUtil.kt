package com.aoc4456.radarchart.util

import com.aoc4456.radarchart.component.chart.radardataset.BaseRadarDataSet
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarEntry

object ChartDataUtil {
    fun getRadarDataWithTheSameValue(
        color: Int,
        numberOfItems: Int,
        value: Float = 60f
    ): RadarData {
        val radarEntryList = mutableListOf<RadarEntry>()
        repeat(numberOfItems) {
            radarEntryList.add(RadarEntry(value))
        }

        val radarDataSet = BaseRadarDataSet(radarEntryList)
        radarDataSet.setColorAndFillColor(color)
        return RadarData(radarDataSet)
    }

    fun getRadarDataFromValues(
        color: Int,
        values: List<Int>
    ): RadarData {
        val radarEntryList = mutableListOf<RadarEntry>()
        values.forEach { value ->
            radarEntryList.add(RadarEntry(value.toFloat()))
        }
        val radarDataSet = BaseRadarDataSet(radarEntryList)
        radarDataSet.setColorAndFillColor(color)
        return RadarData(radarDataSet)
    }

    fun getNPercentValues(value: Int, percent: Int, size: Int): List<Int> {
        val list = mutableListOf<Int>()
        val percentValue = value * percent / 100
        repeat(size) {
            list.add(percentValue)
        }
        return list
    }
}
