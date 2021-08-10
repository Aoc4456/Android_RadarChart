package com.aoc4456.radarchart.component.multiinputrowview

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.component.Stepper
import kotlin.math.pow

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
            stepper.stepValue = calcStep(newValue)
        }

    private var callback: ((InputRowView, Int) -> Unit)? = null

    init {
        inflate(context, R.layout.input_row_view, this)

        labelTextView = findViewById(R.id.labelText)
        editText = findViewById(R.id.editText)
        stepper = findViewById(R.id.stepper)

        // HACK: 入力中にガチャガチャ補正されて見づらい
        editText.doAfterTextChanged { editable ->
            val convertResult = convertEditableToInt(editable, maximum)
            val convertIsFail = !convertResult.first

            if (convertIsFail) {
                editText.setText(convertResult.second.toString())
            }

            this.value = convertResult.second
            stepper.value = value.toDouble()
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
        editText.setText(initialValue.toString())
        this.callback = onChangeValueCallback
    }

    /**
     * TODO テスト
     * editableをIntに変換する
     *
     * 正常に変換できなかった場合、Pair.first で false を返す
     */
    private fun convertEditableToInt(editable: Editable?, maximum: Int): Pair<Boolean, Int> {
        if (editable == null) return Pair(false, 0)

        val text = editable.toString()
        if (text.isEmpty()) {
            return Pair(false, 0)
        }

        var intValue = 0
        try {
            intValue = text.toInt()
        } catch (e: ClassCastException) {
            return Pair(false, 0)
        }

        if (intValue > maximum) {
            return Pair(false, maximum)
        }
        return Pair(true, intValue)
    }

    /**
     * 最大値から桁数を決める
     * TODO テスト
     */
    private fun calcStep(maximum: Int): Double {
        val numberOfDigits = maximum.toString().length
        if (numberOfDigits <= 2) {
            return 1.0
        } else {
            return 10.0.pow((numberOfDigits - 2).toDouble())
        }
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
