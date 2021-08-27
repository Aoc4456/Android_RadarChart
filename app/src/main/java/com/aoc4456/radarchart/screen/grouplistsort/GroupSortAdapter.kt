package com.aoc4456.radarchart.screen.grouplistsort

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.databinding.GroupSortItemBinding
import com.aoc4456.radarchart.util.ChartDataUtil

class GroupSortAdapter(
    private val viewModel: GroupSortViewModel,
    private val itemTouchHelper: ItemTouchHelper
) :
    RecyclerView.Adapter<GroupSortAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GroupSortItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = viewModel.groupList[position]
        holder.binding.let {
            it.title.text = item.group.title
            it.rate.text = item.group.rate.toString()
            val radarData = ChartDataUtil.getRadarDataWithTheSameValue(
                color = item.group.color,
                numberOfItems = item.labelList.size
            )
            it.radarChart.data = radarData
            it.radarChart.notifyDataSetChanged()

            it.dragHandle.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    itemTouchHelper.startDrag(holder)
                }
                return@setOnTouchListener true
            }
        }
    }

    class ViewHolder(val binding: GroupSortItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return viewModel.groupList.size
    }

    fun moveItem(from: Int, to: Int) {
        viewModel.onMoveItem(from, to)
    }
}

class GroupItemTouchCallback : ItemTouchHelper.SimpleCallback(UP or DOWN, 0) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val adapter = recyclerView.adapter as GroupSortAdapter
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition

        adapter.moveItem(from, to)
        adapter.notifyItemMoved(from, to)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ACTION_STATE_DRAG) {
            viewHolder?.itemView?.let {
                it.elevation = 12f
                it.alpha = 0.8f
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.let {
            it.elevation = 6f
            it.alpha = 1f
        }
    }
}
