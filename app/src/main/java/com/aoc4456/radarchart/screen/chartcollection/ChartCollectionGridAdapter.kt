package com.aoc4456.radarchart.screen.chartcollection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.databinding.ChartCollectionGridItemBinding
import com.aoc4456.radarchart.datasource.database.MyChartWithValue
import com.aoc4456.radarchart.util.ChartDataUtil
import com.aoc4456.radarchart.util.ImageUtil

class ChartCollectionGridAdapter(private val viewModel: ChartCollectionViewModel) :
    ListAdapter<IndexedMyChart, ChartCollectionGridAdapter.ViewHolder>(
        ChartCollectionDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.myChartWithValue, viewModel)
    }

    class ViewHolder private constructor(val binding: ChartCollectionGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyChartWithValue, viewModel: ChartCollectionViewModel) {
            binding.myChart = item.myChart

            item.myChart.iconImage?.let {
                binding.iconView.setImageBitmap(ImageUtil.byteArrayToBitmap(it))
            }

            val radarData = ChartDataUtil.getRadarDataFromValues(
                item.myChart.color,
                item.values.map { it.value.toInt() }
            )
            binding.radarChart.let { radarChart ->
                radarChart.data = radarData
                radarChart.yAxis.axisMaximum =
                    viewModel.groupData.value!!.group.maximumValue.toFloat()
                radarChart.setChartItemLabel(viewModel.groupData.value!!.labelList.map { it.text })
                radarChart.notifyDataSetChanged()
            }

            binding.valueLabel.let {
                it.text = ChartCollectionUtil.getTextForGridComment(
                    it.context,
                    viewModel.groupData.value!!,
                    item
                )
            }

            binding.frameForClick.setOnClickListener {
                viewModel.onClickCollectionItem(item)
            }
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChartCollectionGridItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
