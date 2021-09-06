package com.aoc4456.radarchart.component.chart.radarchart

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.aoc4456.radarchart.R
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

/**
 * カスタムRadarChartクラス
 *
 * 使い方
 * 1. xml で app:chartTypeを設定
 * 2. 動的にチャートの状態を変更させる場合は、各BindingAdapterを設定する
 */
class CustomRadarChart(context: Context, attrs: AttributeSet) :
    RadarChart(context, attrs),
    RadarChartInput {

    private var chartType = ChartType.DEFAULT

    init {
        isRotationEnabled = false
        legend.isEnabled = false
        isHighlightPerTapEnabled = false
        description.isEnabled = false

        xAxis.valueFormatter = IndexAxisValueFormatter()
        xAxis.textColor = ContextCompat.getColor(context, R.color.chart_label_color)

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

    override fun applyChartType(type: ChartType) {
        this.chartType = type
        when (type) {
            ChartType.GROUP_CREATE -> {
                fixedLongestLabel = "12345"
                xAxis.textSize = 12F
            }
            ChartType.CHART_CREATE -> {
                fixedLongestLabel = "12345"
                xAxis.textSize = 12F
                xAxis.setMultiLineLabel(true)
            }
            ChartType.GROUP_LIST -> {
                yAxis.setLabelCount(4, true)
                yAxis.axisMaximum = 90f
                fixedLongestLabel = "123"
            }
            ChartType.CHART_COLLECTION_LIST -> {
                fixedLongestLabel = "1234"
                xAxis.textSize = 8F
            }
            ChartType.CHART_COLLECTION_GRID -> {
                fixedLongestLabel = "1234"
                xAxis.textSize = 8F
            }
            else -> {
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
    CHART_CREATE(2),
    CHART_COLLECTION_LIST(3),
    CHART_COLLECTION_GRID(4);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}

interface RadarChartInput {
    fun setChartItemLabel(labels: List<String>)
    fun applyChartType(type: ChartType)
}

/**
 * [BindingAdapter]s for the RadarChart
 */

@BindingAdapter("radarData")
fun setRadarData(radarChart: CustomRadarChart, radarData: RadarData) {
    radarChart.data = radarData
}

@BindingAdapter("labels")
fun setRadarChartLabel(radarChart: CustomRadarChart, labels: List<String>) {
    radarChart.setChartItemLabel(labels)
}

@BindingAdapter("maximum")
fun setRadarChartMaximum(radarChart: CustomRadarChart, maximum: Int) {
    radarChart.yAxis.axisMaximum = maximum.toFloat()
}

@BindingAdapter("notifyDataSetChanged")
fun notifyDataSetChanged(radarChart: CustomRadarChart, update: Boolean) {
    if (update) {
        radarChart.notifyDataSetChanged()
        radarChart.invalidate()
    }
}
