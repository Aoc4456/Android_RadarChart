package com.aoc4456.radarchart.component.multiinputrowview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.aoc4456.radarchart.R

/**
 * 項目名ラベル、EditText、Stepperを１行にまとめたView
 *
 * MultiInputRowView によってホストされる
 */
class InputRowView(
    context: Context,
    private val attrs: AttributeSet
) : LinearLayout(context, attrs) {

    init {
        inflate(context, R.layout.input_row_view, this)
    }
}
