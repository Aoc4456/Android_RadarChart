package com.aoc4456.radarchart.screen.testscreen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aoc4456.radarchart.databinding.FragmentTestBinding
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class TestFragment : Fragment() {

    private lateinit var binding: FragmentTestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarCloseButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.radarChart.let {
            it.isRotationEnabled = false
            it.legend.isEnabled = false
            it.isHighlightPerTapEnabled = false

            it.xAxis.valueFormatter = TestXAxisFormatter()

            it.yAxis.setLabelCount(6, true)
            it.yAxis.setDrawLabels(false)
            it.yAxis.axisMinimum = 0F
            it.yAxis.axisMaximum = 10f
        }

        val radarChartEntry = mutableListOf<RadarEntry>(
            RadarEntry(3f),
            RadarEntry(2f),
            RadarEntry(6f),
            RadarEntry(4f),
            RadarEntry(5f),
        )

        // ラベルを変えるテスト
        (binding.radarChart.xAxis.valueFormatter as IndexAxisValueFormatter).values =
            listOf("項目1", "項目2", "項目3", "項目4", "項目5").toTypedArray()

        val radarChartDataSet = RadarDataSet(radarChartEntry, "ラベルです")
        radarChartDataSet.color = Color.BLUE
        radarChartDataSet.fillColor = Color.BLUE
        radarChartDataSet.setDrawFilled(true)
        radarChartDataSet.valueFormatter = IndexAxisValueFormatter()

        val radarData = RadarData(radarChartDataSet)

        binding.radarChart.data = radarData
        binding.radarChart.data.notifyDataChanged()
        binding.radarChart.notifyDataSetChanged()
    }
}

class TestXAxisFormatter : IndexAxisValueFormatter(
    listOf(
        "aa",
        "bb",
        "cc",
        "dd",
        "ee"
    )
)
