package com.aoc4456.radarchart.screen.grouplistsort

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.databinding.GroupSortItemBinding

class GroupSortAdapter(private val viewModel: GroupSortViewModel) :
    RecyclerView.Adapter<GroupSortAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GroupSortItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = viewModel.groupList[position]
        holder.binding.let {
            it.title.text = item.group.title
        }
    }

    class ViewHolder(val binding: GroupSortItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return viewModel.groupList.size
    }
}
