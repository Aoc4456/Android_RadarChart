package com.aoc4456.radarchart.screen.itemsort

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.databinding.ItemSortListItemBinding
import com.aoc4456.radarchart.util.SortableAdapter

class ItemSortAdapter(
    private val viewModel: ItemSortViewModel,
    private val itemTouchHelper: ItemTouchHelper
) : RecyclerView.Adapter<ItemSortAdapter.ViewHolder>(), SortableAdapter {

    class ViewHolder(val binding: ItemSortListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSortListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = viewModel.labelList[position]
        holder.binding.let {
            it.label.text = item.text
            it.dragHandle.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    itemTouchHelper.startDrag(holder)
                }
                return@setOnTouchListener true
            }
        }
    }

    override fun getItemCount(): Int = viewModel.labelList.size

    override fun moveItem(from: Int, to: Int) {
        viewModel.onMoveItem(from, to)
    }
}
