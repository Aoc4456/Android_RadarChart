package com.aoc4456.radarchart.screen.chartcollection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.component.chart.radarchart.CustomRadarChart
import com.aoc4456.radarchart.datasource.database.MyChartWithValue
import com.aoc4456.radarchart.util.ChartDataUtil

class ChartCollectionAdapter(private val viewModel: ChartCollectionViewModel) :
    ListAdapter<MyChartWithValue, ChartCollectionAdapter.ViewHolder>(ChartCollectionDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView: View = when (viewModel.viewType) {
            ChartCollectionType.LIST -> {
                layoutInflater.inflate(R.layout.chart_collection_list_item, parent, false)
            }
            ChartCollectionType.GRID -> {
                layoutInflater.inflate(R.layout.chart_collection_list_item, parent, false)
            }
        }
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val radarData = ChartDataUtil.getRadarDataFromValues(
            item.myChart.color,
            item.values.map { it.value.toInt() }
        )

        holder.let {
            it.radarChart.let { chart ->
                chart.data = radarData
                // TODO 正しい値をセット
                chart.yAxis.axisMaximum = viewModel.maximum.toFloat()
                // TODO ラベルをセット
                chart.notifyDataSetChanged()
            }
            it.titleView.text = item.myChart.title
            it.commentView.text = item.myChart.comment
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radarChart: CustomRadarChart = itemView.findViewById(R.id.radarChart)
        val titleView: TextView = itemView.findViewById(R.id.title)
        val commentView: TextView = itemView.findViewById(R.id.comment)
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

enum class ChartCollectionType {
    LIST,
    GRID
}
