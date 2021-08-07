package com.aoc4456.radarchart.component.multiinputrowview

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
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

    private val labelTextView: TextView
    private val editText: EditText
    private val stepper: Stepper

    private var value = 0

    private var maximum = 100
        set(newValue) {
            field = newValue
            stepper.maximumValue = newValue.toDouble()
        }

    private var callback: ((InputRowView, Int) -> Unit)? = null

    init {
        inflate(context, R.layout.input_row_view, this)

        labelTextView = findViewById(R.id.labelText)
        editText = findViewById(R.id.editText)
        stepper = findViewById(R.id.stepper)

        editText.doAfterTextChanged { editable ->
            var newValue = 0
            try {
                if (editable.toString() == "") {
                    newValue = 0
                }
            } catch (e: Exception) {
                editText.setText(value.toString())
                return@doAfterTextChanged
            }

            if (newValue > maximum) {
                newValue = maximum
                editText.setText(newValue.toString())
            }
            stepper.value = newValue.toDouble()
            callback?.invoke(this, value)
        }

        stepper.setOnStepperClickListener { _, newValue ->
            this.value = newValue.toInt()
            editText.setText(value.toString())
            callback?.invoke(this, value)
        }
    }

    override fun setup(
        labelName: String,
        initialValue: Int,
        maximum: Int,
        onChangeValueCallback: (view: InputRowView, newValue: Int) -> Unit
    ) {
        labelTextView.text = labelName
        this.maximum = maximum
        this.value = initialValue
        this.callback = onChangeValueCallback
    }
}

interface InputRowViewInput {
    fun setup(
        labelName: String,
        initialValue: Int,
        maximum: Int,
        onChangeValueCallback: (view: InputRowView, newValue: Int) -> Unit
    )
}
