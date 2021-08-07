package com.aoc4456.radarchart.component.multiinputrowview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.aoc4456.radarchart.util.setMargin

class MultiInputRowView(
    context: Context,
    private val attrs: AttributeSet
) : LinearLayout(context, attrs) {

    val scale = resources.displayMetrics.density

    init {
        orientation = VERTICAL
        repeat(5) {
            val rowView = InputRowView(context, attrs)
            rowView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, (36 * scale + 0.5f).toInt())
            placeView(rowView)
        }
    }

    private fun placeView(view: InputRowView) {
        addView(view)
        val topMargin = (5 * scale + 0.5f).toInt()
        view.setMargin(top = topMargin)
    }
}
