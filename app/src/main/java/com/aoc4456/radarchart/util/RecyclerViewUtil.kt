package com.aoc4456.radarchart.util

import android.content.Context
import android.content.res.Configuration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aoc4456.radarchart.screen.chartcollection.CollectionType
import com.aoc4456.radarchart.screen.grouplistsort.GroupSortAdapter
import timber.log.Timber
import kotlin.math.min

fun calcSpanCountBasedOnScreenSize(context: Context, type: CollectionType): Int {
    val orientation = context.resources.configuration.orientation
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density

    Timber.d("画面幅 = $dpWidth")
    when (type) {
        CollectionType.LIST -> {
            return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                return if (dpWidth < 600) {
                    1
                } else {
                    2
                }
            } else {
                2
            }
        }
        CollectionType.GRID -> {
            return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                val base = (dpWidth / 120).toInt()
                min(base, 4)
            } else {
                val base = (dpWidth / 150).toInt()
                min(base, 5)
            }
        }
    }
}

class ListItemTouchCallback :
    ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
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
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
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
