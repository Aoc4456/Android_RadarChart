package com.aoc4456.radarchart.component

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import com.aoc4456.radarchart.R

class Stepper(context: Context, private val attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var listener: StepperOutput? = null

    private val plusButton: Button
    private val minusButton: Button

    var minimumValue: Double = 0.0
        set(newValue) {
            field = newValue
            if (value < minimumValue) {
                value = minimumValue
            }
        }

    var maximumValue: Double = 100.0
        set(newValue) {
            field = newValue
            if (maximumValue < newValue) {
                value = maximumValue
            }
        }

    var stepValue: Double = 1.0

    var value: Double = 0.0
        set(newValue) {
            field = when {
                newValue < minimumValue -> {
                    minimumValue
                }
                maximumValue < newValue -> {
                    maximumValue
                }
                else -> {
                    newValue
                }
            }
            setButtonEnable()
        }

    init {
        inflate(context, R.layout.stepper, this)
        plusButton = findViewById(R.id.plusButton)
        minusButton = findViewById(R.id.minusButton)

        setButtonEnable()

        plusButton.setOnClickListener {
            value += stepValue
            listener?.onTapStepperButton(value)
        }
        minusButton.setOnClickListener {
            value -= stepValue
            listener?.onTapStepperButton(value)
        }
    }

    private fun setButtonEnable() {
        plusButton.isEnabled = value < maximumValue
        minusButton.isEnabled = value > minimumValue
    }

    fun setStepperListener(listener: StepperOutput) {
        this.listener = listener
    }
}

interface StepperOutput {
    fun onTapStepperButton(value: Double)
}
