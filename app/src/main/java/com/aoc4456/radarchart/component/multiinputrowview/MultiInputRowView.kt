package com.aoc4456.radarchart.component.multiinputrowview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.aoc4456.radarchart.util.setMargin

class MultiInputRowView(
    context: Context,
    private val attrs: AttributeSet
) : LinearLayout(context, attrs), MultiInputViewInput {

    private val scale = resources.displayMetrics.density
    private val rowViewList = mutableListOf<InputRowView>()

    private var maximum: Int = 100
    lateinit var onChangeValueCallback: (index: Int, newValue: Int) -> Unit

    private val defaultValue get() = (maximum * 0.6).toInt()

    init {
        orientation = VERTICAL
    }

    private fun createRowView(label: String, value: Int?): InputRowView {
        val rowView = InputRowView(context, attrs)

        val rowHeight = (36 * scale + 0.5f).toInt()
        rowView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, rowHeight)

        rowView.setup(
            labelName = label,
            initialValue = value ?: defaultValue,
            maximum = this.maximum,
            onChangeValueCallback = { view: InputRowView, newValue: Int ->
                val index = rowViewList.indexOf(view)
                this.onChangeValueCallback(index, newValue)
            }
        )
        return rowView
    }

    private fun placeView(view: InputRowView) {
        addView(view)
        val topMargin = (5 * scale + 0.5f).toInt()
        view.setMargin(top = topMargin)
    }

    override fun setup(
        labels: List<String>,
        initialValues: List<Int>?,
        maximum: Int,
        onChangeValueCallback: (index: Int, newValue: Int) -> Unit
    ) {
        this.maximum = maximum
        this.onChangeValueCallback = onChangeValueCallback
        for (i in labels.indices) {
            val rowView = createRowView(labels[i], initialValues?.getOrNull(i))
            placeView(rowView)
            rowViewList.add(rowView)
        }
    }
}

interface MultiInputViewInput {
    fun setup(
        labels: List<String>,
        initialValues: List<Int>?,
        maximum: Int,
        onChangeValueCallback: (index: Int, newValue: Int) -> Unit
    )
}
