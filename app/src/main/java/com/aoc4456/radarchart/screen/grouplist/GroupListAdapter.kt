package com.aoc4456.radarchart.screen.grouplist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.datasource.database.ChartGroup

class GroupListAdapter(private val viewModel: GroupListViewModel) :
    ListAdapter<ChartGroup, GroupListAdapter.ViewHolder>(ChartGroupDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
}


class ChartGroupDiffCallBack : DiffUtil.ItemCallback<ChartGroup>() {
    override fun areItemsTheSame(oldItem: ChartGroup, newItem: ChartGroup): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChartGroup, newItem: ChartGroup): Boolean {
        return oldItem == newItem
    }
}