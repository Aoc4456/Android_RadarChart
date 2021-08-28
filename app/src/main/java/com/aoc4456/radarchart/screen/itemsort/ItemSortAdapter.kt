package com.aoc4456.radarchart.screen.itemsort

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.databinding.ItemSortListItemBinding

class ItemSortAdapter(
    private val viewModel: ItemSortViewModel,
    private val itemTouchHelper: ItemTouchHelper
) : RecyclerView.Adapter<ItemSortAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSortListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSortListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = viewModel.labelList[position]
        holder.binding.label.text = item.text
    }

    override fun getItemCount(): Int = viewModel.labelList.size
}
