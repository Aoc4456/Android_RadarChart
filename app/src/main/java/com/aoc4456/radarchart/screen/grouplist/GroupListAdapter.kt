package com.aoc4456.radarchart.screen.grouplist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.databinding.ChartGroupListItemBinding
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts

class GroupListAdapter(private val viewModel: GroupListViewModel) :
    ListAdapter<GroupWithLabelAndCharts, GroupListAdapter.ViewHolder>(ChartGroupDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding: ChartGroupListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GroupWithLabelAndCharts) {
            binding.chartGroup = item
            binding.executePendingBindings()
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChartGroupListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ChartGroupDiffCallBack : DiffUtil.ItemCallback<GroupWithLabelAndCharts>() {
    override fun areItemsTheSame(
        oldItem: GroupWithLabelAndCharts,
        newItem: GroupWithLabelAndCharts
    ): Boolean {
        return oldItem.group.id == newItem.group.id
    }

    override fun areContentsTheSame(
        oldItem: GroupWithLabelAndCharts,
        newItem: GroupWithLabelAndCharts
    ): Boolean {
        return oldItem == newItem
    }
}
