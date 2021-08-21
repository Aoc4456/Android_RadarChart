package com.aoc4456.radarchart.screen.chartcollection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.databinding.ChartCollectionListItemBinding
import com.aoc4456.radarchart.datasource.database.MyChartWithValue
import com.aoc4456.radarchart.util.ChartDataUtil

class ChartCollectionListAdapter(private val viewModel: ChartCollectionViewModel) :
    ListAdapter<MyChartWithValue, ChartCollectionListAdapter.ViewHolder>(ChartCollectionDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel)
    }

    class ViewHolder private constructor(val binding: ChartCollectionListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyChartWithValue, viewModel: ChartCollectionViewModel) {
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
            binding.title.text = item.myChart.title
            binding.comment.text = item.myChart.comment
            binding.total.let { totalView ->
                val sum = item.values.map { it.value }.sum().toInt().toString()
                totalView.text = totalView.resources.getString(R.string.total_with_value, sum)
            }

            binding.frameForClick.setOnClickListener {
                viewModel.onClickCollectionItem(item)
            }
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChartCollectionListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ChartCollectionDiffCallBack : DiffUtil.ItemCallback<MyChartWithValue>() {
    override fun areItemsTheSame(
        oldItem: MyChartWithValue,
        newItem: MyChartWithValue
    ): Boolean {
        return oldItem.myChart.id == newItem.myChart.id
    }

    override fun areContentsTheSame(
        oldItem: MyChartWithValue,
        newItem: MyChartWithValue
    ): Boolean {
        return oldItem == newItem
    }
}
