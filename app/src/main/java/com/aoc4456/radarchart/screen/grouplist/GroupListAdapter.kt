package com.aoc4456.radarchart.screen.grouplist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.databinding.ChartGroupListItemBinding
import com.aoc4456.radarchart.datasource.database.ChartGroup

class GroupListAdapter(private val viewModel: GroupListViewModel) :
    ListAdapter<ChartGroup, GroupListAdapter.ViewHolder>(ChartGroupDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    class ViewHolder private constructor(binding: ChartGroupListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun createViewHolder(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChartGroupListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ChartGroupDiffCallBack : DiffUtil.ItemCallback<ChartGroup>() {
    override fun areItemsTheSame(oldItem: ChartGroup, newItem: ChartGroup): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChartGroup, newItem: ChartGroup): Boolean {
        return oldItem == newItem
    }
}
