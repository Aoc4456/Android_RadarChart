package com.aoc4456.radarchart.component.chart.radarchart

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import com.aoc4456.radarchart.R
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

/**
 * RadarChartのBaseクラス
 *
 * 使い方
 * 1. xml で app:chartTypeを設定
 * 2. 動的にチャートの状態を変更させる場合は、各BindingAdapterを設定する
 */
open class BaseRadarChart(context: Context, attrs: AttributeSet) :
    RadarChart(context, attrs),
    RadarChartInput {

    private var chartType = ChartType.DEFAULT

    init {
        isRotationEnabled = false
        legend.isEnabled = false
        isHighlightPerTapEnabled = false
        description.isEnabled = false

        xAxis.valueFormatter = IndexAxisValueFormatter()

        yAxis.setDrawLabels(false)
        yAxis.setLabelCount(6, true)
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 100f

        // get custom property
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BaseRadarChart,
            0, 0
        ).apply {
            try {
                val chartTypeInt =
                    getInteger(R.styleable.BaseRadarChart_chartType, ChartType.DEFAULT.value)
                chartType = ChartType.fromInt(chartTypeInt)
            } finally {
                recycle()
            }
        }

        applyChartType(chartType)
    }

    fun applyChartType(type: ChartType) {
        this.chartType = type
        when (type) {
            ChartType.GROUP_LIST -> {
                yAxis.setLabelCount(4, true)
                yAxis.axisMaximum = 90f
                fixedLongestLabel = "12345"
            }
        }
    }

    override fun setChartItemLabel(labels: List<String>) {
        (xAxis.valueFormatter as IndexAxisValueFormatter).values = labels.toTypedArray()
    }
}

enum class ChartType(val value: Int) {
    DEFAULT(-1),
    GROUP_LIST(0),
    GROUP_CREATE(1),
    CHART_CREATE(2);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}

interface RadarChartInput {
    fun setChartItemLabel(labels: List<String>)
}

/**
 * [BindingAdapter]s for the RadarChart
 */

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
