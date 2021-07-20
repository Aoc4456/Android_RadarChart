package com.aoc4456.radarchart.component.chart.radarchart

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

/**
 * RadarChartのBaseクラス
 *
 * 分割数 : 6
 * 最大値 : 100
 */
open class BaseRadarChart(context: Context, attrs: AttributeSet) :
    RadarChart(context, attrs),
    RadarChartInput {

    init {
        isRotationEnabled = false
        legend.isEnabled = false
        isHighlightPerTapEnabled = false

        xAxis.valueFormatter = IndexAxisValueFormatter()

        yAxis.setDrawLabels(false)
        yAxis.setLabelCount(6, true)
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 100f
    }

    override fun setChartItemLabel(labels: List<String>) {
        (xAxis.valueFormatter as IndexAxisValueFormatter).values = labels.toTypedArray()
    }
}

interface RadarChartInput {
    fun setChartItemLabel(labels: List<String>)
}

/**
 * [BindingAdapter]s for the RadarChart
 */

@BindingAdapter("radarDataAndLabels")
fun setDataAndLabel(radarChart: BaseRadarChart, data: Pair<RadarData, List<String>>) {
    radarChart.data = data.first
    radarChart.setChartItemLabel(data.second)
    radarChart.notifyDataSetChanged()
    radarChart.invalidate()
}

@BindingAdapter("radarData")
fun setRadarData(radarChart: BaseRadarChart, radarData: RadarData) {
    radarChart.data = radarData
}

@BindingAdapter("labels")
fun setRadarChartLabel(radarChart: BaseRadarChart, labels: List<String>) {
    radarChart.setChartItemLabel(labels)
}

@BindingAdapter("notifyDataSetChanged")
fun notifyDataSetChanged(radarChart: BaseRadarChart, update: Boolean) {
    if (update) {
        radarChart.notifyDataSetChanged()
        radarChart.invalidate()
    }
}
