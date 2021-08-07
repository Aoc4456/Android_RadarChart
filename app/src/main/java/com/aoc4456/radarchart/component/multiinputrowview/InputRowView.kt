package com.aoc4456.radarchart.component.multiinputrowview

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.component.Stepper

/**
 * 項目名ラベル、EditText、Stepperを１行にまとめたView
 *
 * MultiInputRowView によってホストされる
 */
class InputRowView(
    context: Context,
    private val attrs: AttributeSet
) : LinearLayout(context, attrs), InputRowViewInput {

    lateinit var labelTextView: TextView
    lateinit var editText: EditText
    lateinit var stepper: Stepper

    init {
        inflate(context, R.layout.input_row_view, this)

        labelTextView = findViewById(R.id.labelText)
        editText = findViewById(R.id.editText)
        stepper = findViewById(R.id.stepper)
    }

    override fun setLabel(label: String) {
        labelTextView.text = label
    }

    override fun setValue(value: Double) {
        editText.setText(value.toInt().toString())
        stepper.value = value
    }

    override fun setMaximum(maximum: Int) {
        stepper.maximumValue = maximum.toDouble()
    }
}

interface InputRowViewInput {
    fun setLabel(label: String)
    fun setValue(value: Double)
    fun setMaximum(maximum: Int)
}

interface InputRowViewOutput {
    fun onChangeValue(view: InputRowView, value: Double)
}
