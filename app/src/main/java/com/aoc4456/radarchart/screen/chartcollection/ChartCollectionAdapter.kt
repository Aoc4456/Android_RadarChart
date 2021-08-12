package com.aoc4456.radarchart.screen.chartcollection

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.datasource.database.MyChartWithValue

class ChartCollectionAdapter(private val viewModel: ChartCollectionViewModel) :
    ListAdapter<MyChartWithValue, ChartCollectionAdapter.ViewHolder>(ChartCollectionDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
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
