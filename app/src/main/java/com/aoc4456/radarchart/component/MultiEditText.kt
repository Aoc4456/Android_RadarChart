package com.aoc4456.radarchart.component

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.LinearLayout
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.util.setMargin

class MultiEditText(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val scale = resources.displayMetrics.density

    init {
        orientation = VERTICAL
        repeat(5) {
            val editText = BorderEditText(context, attrs)
            editText.inputType = InputType.TYPE_CLASS_TEXT
            editText.height = resources.getDimensionPixelSize(R.dimen.edit_text_height)

            val padding = (8 * scale + 0.5f).toInt()

            addView(editText)
            editText.setMargin(top = padding)
        }
    }
}
