package com.aoc4456.radarchart.component.multiinputrowview

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.component.Stepper
import com.aoc4456.radarchart.component.StepperOutput

/**
 * 項目名ラベル、EditText、Stepperを１行にまとめたView
 *
 * MultiInputRowView によってホストされる
 */
class InputRowView(
    context: Context,
    private val attrs: AttributeSet
) : LinearLayout(context, attrs), InputRowViewInput {

    private var listener: InputRowViewOutput? = null

    private var label = ""
    private var value = 0
    private var maximum = 100

    private val labelTextView: TextView
    private val editText: EditText
    private val stepper: Stepper

    init {
        inflate(context, R.layout.input_row_view, this)

        labelTextView = findViewById(R.id.labelText)
        editText = findViewById(R.id.editText)
        stepper = findViewById(R.id.stepper)

        editText.doAfterTextChanged { editable ->
            var newValue = 0
            newValue = try {
                editable.toString().toInt()
            } catch (e: ClassCastException) {
                stepper.value.toInt()
            }

            if (newValue > maximum) {
                newValue = maximum
                editText.setText(newValue.toString())
            }
            stepper.value = newValue.toDouble()
            listener?.onChangeValue(this, newValue)
        }

        stepper.setStepperListener(object : StepperOutput {
            override fun onTapStepperButton(value: Double) {
                editText.setText(value.toInt().toString())
                listener?.onChangeValue(this@InputRowView, value.toInt())
            }
        })
    }

    override fun setValueChangeListener(listener: InputRowViewOutput) {
        this.listener = listener
    }

    override fun setLabel(label: String) {
        this.label = label
        labelTextView.text = label
    }

    override fun setValue(value: Int) {
        this.value = value
        editText.setText(value.toString())
        stepper.value = value.toDouble()
    }

    override fun setMaximum(maximum: Int) {
        this.maximum = maximum
        stepper.maximumValue = maximum.toDouble()
    }
}

interface InputRowViewInput {
    fun setValueChangeListener(listener: InputRowViewOutput)
    fun setLabel(label: String)
    fun setValue(value: Int)
    fun setMaximum(maximum: Int)
}

interface InputRowViewOutput {
    fun onChangeValue(view: InputRowView, value: Int)
}
