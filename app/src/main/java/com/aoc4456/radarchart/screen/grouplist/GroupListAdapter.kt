package com.aoc4456.radarchart.screen.grouplist

import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.databinding.GroupListItemBinding
import com.aoc4456.radarchart.datasource.database.GroupWithLabelAndCharts
import com.aoc4456.radarchart.util.ChartDataUtil

class GroupListAdapter(private val viewModel: GroupListViewModel) :
    ListAdapter<GroupWithLabelAndCharts, GroupListAdapter.ViewHolder>(ChartGroupDiffCallBack()),
    View.OnCreateContextMenuListener {

    var longTappedItem: GroupWithLabelAndCharts? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder.createViewHolder(parent)
        viewHolder.itemView.setOnCreateContextMenuListener(this)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.frameForClick.let {
            it.setOnClickListener {
                viewModel.onClickListItem(item)
            }
            it.setOnLongClickListener {
                longTappedItem = item
                false
            }
        }
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding: GroupListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GroupWithLabelAndCharts) {
            binding.chartGroup = item

            val radarData = ChartDataUtil.getRadarDataWithTheSameValue(
                color = item.group.color,
                numberOfItems = item.labelList.size
            )
            binding.radarChart.data = radarData
            binding.radarChart.notifyDataSetChanged()

            binding.executePendingBindings()
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GroupListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        if (menu == null || v == null) return
        menu.setHeaderTitle(R.string.edit)
        val inflater = MenuInflater(v.context)
        inflater.inflate(R.menu.group_list_context_meu, menu)
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
