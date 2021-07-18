package com.aoc4456.radarchart.component

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import com.aoc4456.radarchart.R
import com.aoc4456.radarchart.util.setMargin
import timber.log.Timber

class MultiEditText(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs),
    MultiEditTextInput {

    private val scale = resources.displayMetrics.density

    private var numberOfItems = 5 // TODO 初期値をどうにかする

    init {
        orientation = VERTICAL
        repeat(numberOfItems) {
            val editText = createEditText(attrs)
            addView(editText)
            val topMargin = (8 * scale + 0.5f).toInt()
            editText.setMargin(top = topMargin)
        }
    }

    private fun createEditText(attrs: AttributeSet): EditText {
        val editText = BorderEditText(context, attrs)
        editText.inputType = InputType.TYPE_CLASS_TEXT
        editText.height = resources.getDimensionPixelSize(R.dimen.edit_text_height)
        return editText
    }

    override fun changeNumberOfItems(textList: List<String>) {
        Timber.d("テキストチェンジ : $textList")
    }
}

interface MultiEditTextInput {
    fun changeNumberOfItems(textList: List<String>)
}

interface MultiEditTextOutput {
    fun onEndEditingMultiEditText(index: Int, text: String)
}

/**
 * [BindingAdapter]s for the MultiEditText
 */

@BindingAdapter("app:textList")
fun changeNumberOfItems(multiEditText: MultiEditText, textList: List<String>) {
    multiEditText.changeNumberOfItems(textList)
}
