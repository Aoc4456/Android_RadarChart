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
import com.aoc4456.radarchart.util.ImageUtil
import com.aoc4456.radarchart.util.SortableAdapter

class GroupSortAdapter(
    private val viewModel: GroupSortViewModel,
    private val itemTouchHelper: ItemTouchHelper
) :
    RecyclerView.Adapter<GroupSortAdapter.ViewHolder>(), SortableAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GroupSortItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = viewModel.groupList[position]
        holder.binding.let { binding ->
            binding.chartGroup = item

            if (item.group.iconImage == null) {
                val radarData = ChartDataUtil.getRadarDataWithTheSameValue(
                    color = item.group.color,
                    numberOfItems = item.labelList.size
                )
                binding.radarChart.data = radarData
                binding.radarChart.notifyDataSetChanged()
            } else {
                val bitmap = ImageUtil.byteArrayToBitmap(item.group.iconImage!!)
                binding.iconView.setImageBitmap(bitmap)
            }

            binding.dragHandle.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    itemTouchHelper.startDrag(holder)
                }
                return@setOnTouchListener true
            }
        }
    }

    class ViewHolder(val binding: GroupSortItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = viewModel.groupList.size

    override fun moveItem(from: Int, to: Int) {
        viewModel.onMoveItem(from, to)
    }
}
